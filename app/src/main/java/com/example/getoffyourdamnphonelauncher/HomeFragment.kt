package com.example.getoffyourdamnphonelauncher

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment(private val primaryColorHex : String, private val backgroundColorHex : String) : Fragment()  {
    private lateinit var layout: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var appRecyclerView: RecyclerView
    private lateinit var favoriteAppsAdaptor: FavoriteAppsAdaptor
    private lateinit var viewBar: View
    private lateinit var editButton: Button
    private lateinit var folderButton: Button
    private lateinit var settingsButton: Button

    private var isEditMode = false

    private lateinit var sharedViewModel: SharedAppData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        layout = view.findViewById(R.id.layout)

        viewBar = view.findViewById(R.id.viewBar)
        editButton = view.findViewById(R.id.editButton)
        folderButton = view.findViewById(R.id.createFolderButton)
        settingsButton = view.findViewById(R.id.launcherSettingsButton)

        setColors(primaryColorHex, backgroundColorHex)

        folderButton.alpha = 0.0f

        sharedViewModel = ViewModelProvider(requireActivity())[SharedAppData::class.java]

        appRecyclerView = view.findViewById(R.id.app_recycler_view)
        appRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        favoriteAppsAdaptor = FavoriteAppsAdaptor(this@HomeFragment, sharedViewModel, primaryColorHex, backgroundColorHex)
        appRecyclerView.adapter = favoriteAppsAdaptor

        sharedViewModel.apps.observe(viewLifecycleOwner) { appList ->
            appList?.let {
                favoriteAppsAdaptor.updateAppList(it)
            }
        }

        editButton.setOnClickListener {
            sharedViewModel.printData()

            isEditMode = !isEditMode
            editButton.text = if (isEditMode) "Done" else "Edit"
            folderButton.alpha = if (isEditMode) 1.0f else 0.0f

            favoriteAppsAdaptor.updateEditMode(isEditMode)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        folderButton.setOnClickListener {
            if (folderButton.alpha == 0.0f) return@setOnClickListener

            Log.i("Create folder button pressed", "")
            //showCreateFolderDialog(appList.value)
        }

        return view
    }

    fun showContextMenu(app: AppData) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.home_fragment_apps_context_menu, null)
        val appTitle: TextView = dialogView.findViewById(R.id.app_title)
        val viewBar: View = dialogView.findViewById(R.id.viewBar)
        val appInfoOption: TextView = dialogView.findViewById(R.id.app_info)
        val favoritesOption: TextView = dialogView.findViewById(R.id.add_to_favorites)
        val uninstallOption: TextView = dialogView.findViewById(R.id.uninstall)
        val renameOption: TextView = dialogView.findViewById(R.id.rename_app)

        dialogView.setBackgroundColor(Color.parseColor(backgroundColorHex))
        appTitle.setTextColor(Color.parseColor(primaryColorHex))
        viewBar.setBackgroundColor(Color.parseColor(primaryColorHex))
        appInfoOption.setTextColor(Color.parseColor(primaryColorHex))
        favoritesOption.setTextColor(Color.parseColor(primaryColorHex))
        uninstallOption.setTextColor(Color.parseColor(primaryColorHex))
        renameOption.setTextColor(Color.parseColor(primaryColorHex))

        appTitle.text = getString(R.string.app_title, app.name, app.originalName)
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
            } else {
                sharedViewModel.addAppAsFavorite(app)
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

        title.text = getString(R.string.simple_title, "Rename App")

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

    private fun setColors(primaryColorHex : String, backgroundColorHex : String) {
        layout.setBackgroundColor(Color.parseColor(backgroundColorHex))

        viewBar.setBackgroundColor(Color.parseColor(primaryColorHex))

        editButton.setBackgroundColor(Color.parseColor(primaryColorHex))
        editButton.setTextColor(Color.parseColor(backgroundColorHex))

        settingsButton.setBackgroundColor(Color.parseColor(primaryColorHex))
        settingsButton.setTextColor(Color.parseColor(backgroundColorHex))

        folderButton.setBackgroundColor(Color.parseColor(primaryColorHex))
        folderButton.setTextColor(Color.parseColor(backgroundColorHex))
    }
}

class FavoriteAppsAdaptor(private val fragment: HomeFragment, private var sharedViewModel: SharedAppData, private val primaryColorHex : String, private val backgroundColorHex : String) : RecyclerView.Adapter<FavoriteAppsAdaptor.AppViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_app, parent, false)
        return AppViewHolder(view)
    }

    private var appList: MutableList<AppData> = mutableListOf()
    private var isEditMode = false

    fun updateEditMode(newMode: Boolean) {
        isEditMode = newMode
        notifyDataSetChanged()
    }

    fun updateAppList(newList: MutableList<AppData>) {
        val tempList: MutableList<AppData> = mutableListOf()

        for (app in newList)
            if (app.favorite)
                tempList.add(app)

        tempList.sortBy { it.favoritePosition }

        appList = tempList
        notifyDataSetChanged()
    }

    private fun getHighestFavoritePosition(): Int {
        return appList.last().favoritePosition
    }

    private fun getLowestFavoritePosition(): Int {
        return appList[0].favoritePosition
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val pm: PackageManager = holder.itemView.context.packageManager
        val app = appList[position]
        holder.appName.text = app.name
        holder.appName.setTextColor(Color.parseColor(primaryColorHex))

        if (isEditMode) {
            holder.buttonContainer.visibility = View.VISIBLE

            holder.buttonUp.setBackgroundColor(Color.parseColor(backgroundColorHex))
            holder.buttonUp.setTextColor(Color.parseColor(primaryColorHex))

            holder.buttonDown.setBackgroundColor(Color.parseColor(backgroundColorHex))
            holder.buttonDown.setTextColor(Color.parseColor(primaryColorHex))

            when (app.favoritePosition) {
                getLowestFavoritePosition() -> {
                    holder.buttonUp.visibility = View.GONE
                }
                getHighestFavoritePosition() -> {
                    holder.buttonDown.alpha = 0.0F
                }
                else -> {
                    holder.buttonDown.alpha = 1.0F
                    holder.buttonUp.visibility = View.VISIBLE
                    holder.buttonDown.visibility = View.VISIBLE
                }
            }
        } else {
            holder.buttonContainer.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            // If editing position
            if (isEditMode) return@setOnClickListener

            // Launch app on tap
            val launchIntent: Intent? = pm.getLaunchIntentForPackage(app.packageName)
            if (launchIntent != null) {
                holder.itemView.context.startActivity(launchIntent)
            }
        }

        holder.itemView.setOnLongClickListener {
            // If editing position
            if (isEditMode) return@setOnLongClickListener false

            // Launch menu on hold
            fragment.showContextMenu(app)
            true
        }

        holder.buttonUp.setOnClickListener {
            // Move app up
            sharedViewModel.favoriteAppUp(app)
        }

        holder.buttonDown.setOnClickListener {
            // Move app down
            sharedViewModel.favoriteAppDown(app)
        }
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appName: TextView = itemView.findViewById(R.id.app_name)

        val buttonContainer: LinearLayout = itemView.findViewById(R.id.buttons_container)
        val buttonUp: Button = itemView.findViewById(R.id.button1)
        val buttonDown: Button = itemView.findViewById(R.id.button2)
    }
}