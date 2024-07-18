package com.example.getoffyourdamnphonelauncher

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.provider.Settings

class AllAppsFragment : Fragment() {

    private lateinit var appSearchBar: EditText
    private lateinit var settingsButton: ImageButton
    private lateinit var appRecyclerView: RecyclerView
    private lateinit var allAppsAdaptor: AllAppsAdaptor
    private lateinit var sharedViewModel: SharedAppData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_apps, container, false)

        appSearchBar = view.findViewById(R.id.app_search_bar)
        settingsButton = view.findViewById(R.id.settings_button)
        appRecyclerView = view.findViewById(R.id.app_recycler_view)

        appRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        allAppsAdaptor = AllAppsAdaptor(this@AllAppsFragment)
        appRecyclerView.adapter = allAppsAdaptor

        sharedViewModel = ViewModelProvider(requireActivity())[SharedAppData::class.java]

        sharedViewModel.apps.observe(viewLifecycleOwner) { appList ->
            appList?.let {
                allAppsAdaptor.updateAppList(it)
            }
        }

        appSearchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val filteredList = sharedViewModel.apps.value
                    ?.filter {
                        it.name.contains(s.toString(), ignoreCase = true)
                    }?.toMutableList() ?: mutableListOf()
                allAppsAdaptor.updateAppList(filteredList)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        appSearchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        settingsButton.setOnClickListener() {
            val intent = Intent(Settings.ACTION_SETTINGS)
            startActivity(intent)
        }

        return view
    }

    fun clearSearchBarAndHideKeyboard() {
        // clears the search text from the search bards
        appSearchBar.text.clear()
        // removes focus from search bar
        appSearchBar.clearFocus()
        // hides the on screen keyboard
        hideKeyboard()
    }

    private fun hideKeyboard() {
        // hides the on screen keyboard
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(appSearchBar.windowToken, 0)
    }

    fun showContextMenu(app: AppData) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.all_apps_context_menu, null)
        val appTitle: TextView = dialogView.findViewById(R.id.app_title)
        val appInfoOption: TextView = dialogView.findViewById(R.id.app_info)
        val favoritesOption: TextView = dialogView.findViewById(R.id.add_to_favorites)
        val uninstallOption: TextView = dialogView.findViewById(R.id.uninstall)
        val renameOption: TextView = dialogView.findViewById(R.id.rename_app)

        appTitle.text = app.name + " (" + app.originalName + ")"
        val isFavorite = sharedViewModel.isAppFavorite(app)

        favoritesOption.text = if (isFavorite) "Remove from Favorites" else "Add to Favorites"

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        // Opens and displays the app info
        appInfoOption.setOnClickListener {
            val packageName = app.packageName

            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:$packageName")
            }

            startActivity(intent)

            dialog.dismiss()
        }

        // Adds or removes from favorites
        favoritesOption.setOnClickListener {
            if (isFavorite) {
                sharedViewModel.removeAppAsFavorite(app)
                //appDataActions.removeAppAsFavorite(app)
            } else {
                sharedViewModel.addAppAsFavorite(app)
                //appDataActions.addAppAsFavorite(app)
            }

            dialog.dismiss()
        }

        // Uninstalls app
        uninstallOption.setOnClickListener {
            val packageName = app.packageName

            val intent = Intent(Intent.ACTION_DELETE).apply {
                data = Uri.parse("package:$packageName")
            }

            startActivity(intent)

            dialog.dismiss()
        }

        // Renames app
        renameOption.setOnClickListener {
            showRenameDialog(app)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showRenameDialog(appInfo: AppData) {
        val renameDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.rename_app_dialog, null)
        val editText = renameDialogView.findViewById<EditText>(R.id.textRename)
        val title: TextView = renameDialogView.findViewById(R.id.app_title)
        val btnCancel = renameDialogView.findViewById<Button>(R.id.btnCancel)
        val btnSave = renameDialogView.findViewById<Button>(R.id.btnSave)

        title.text = "Rename App"

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(renameDialogView)
            .create()

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            val newName = editText.text.toString().trim()
            if (newName.isNotEmpty()) {
                sharedViewModel.renameApp(appInfo, newName)
                dialog.dismiss()
            } else {
                editText.error = "Name cannot be empty"
            }
        }

        dialog.show()
    }
}


class AllAppsAdaptor(private val fragment: AllAppsFragment) : RecyclerView.Adapter<AllAppsAdaptor.AppViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    private var appList: MutableList<AppData> = mutableListOf()

    fun updateAppList(newList: MutableList<AppData>) {
        appList = newList
        appList.sortBy { it.name }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val pm: PackageManager = holder.itemView.context.packageManager
        val app = appList[position]
        holder.appName.text = app.name

        holder.itemView.setOnClickListener {
            // Launch app on tap
            val launchIntent: Intent? = pm.getLaunchIntentForPackage(app.packageName)
            if (launchIntent != null) {
                fragment.clearSearchBarAndHideKeyboard()
                holder.itemView.context.startActivity(launchIntent)
            }
        }

        holder.itemView.setOnLongClickListener {
            // Launch menu on hold
            fragment.showContextMenu(app)
            true
        }
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appName: TextView = itemView.findViewById(R.id.app_name)
    }
}