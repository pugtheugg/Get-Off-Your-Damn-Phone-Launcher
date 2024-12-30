package com.example.getoffyourdamnphonelauncher

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsActivity : AppCompatActivity() {

    private lateinit var layout: LinearLayout
    private lateinit var title: TextView
    private lateinit var viewBar: View
    private lateinit var colorPrimaryLabel: TextView
    private lateinit var colorBackgroundLabel: TextView
    private lateinit var btnBack: ImageButton

    private lateinit var toggleEditColors: TextView
    private lateinit var collapsibleEditColorsContent: LinearLayout

    private lateinit var toggleThemes: TextView
    private lateinit var collapsibleThemes: GridLayout
    private lateinit var hellThemeButton: Button
    private lateinit var forestThemeButton: Button
    private lateinit var darculaThemeButton: Button
    private lateinit var heavenThemeButton: Button
    private lateinit var ikeaThemeButton: Button
    private lateinit var berryThemeButton: Button

    lateinit var prefs: SharedPreferences

    private var primaryColorHex: String = ""
    private var backgroundColorHex: String = ""

    private var isEditColorsContentVisible = false
    private var isThemeLayoutVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        layout = findViewById(R.id.linear_layout)

        title = findViewById(R.id.title)
        btnBack = findViewById(R.id.btnBack)

        viewBar = findViewById(R.id.view_bar)

        colorPrimaryLabel = findViewById(R.id.color_primary_label)
        colorBackgroundLabel = findViewById(R.id.color_background_label)

        toggleEditColors = findViewById(R.id.toggleEditColors)
        toggleThemes = findViewById(R.id.toggleThemes)

        collapsibleEditColorsContent = findViewById(R.id.collapsibleEditColorsContent)
        collapsibleThemes = findViewById(R.id.themeLayout)

        hellThemeButton = findViewById(R.id.hellThemeButton)
        forestThemeButton = findViewById(R.id.forestThemeButton)
        darculaThemeButton = findViewById(R.id.draculaThemeButton)

        heavenThemeButton = findViewById(R.id.heavenThemeButton)
        ikeaThemeButton = findViewById(R.id.ikeaThemeButton)
        berryThemeButton = findViewById(R.id.berryThemeButton)

        prefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val defaultPrimaryColor = String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(this, R.color.colorPrimary))
        val defaultBackgroundColor = String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(this, R.color.colorBackground))

        primaryColorHex = prefs.getString("colorPrimary", defaultPrimaryColor) ?: defaultPrimaryColor
        backgroundColorHex = prefs.getString("colorBackground", defaultBackgroundColor) ?: defaultBackgroundColor

        // Set initial values in EditTexts
        updateColors(primaryColorHex, backgroundColorHex)

        toggleEditColors.setOnClickListener {
            if (isEditColorsContentVisible) {
                collapsibleEditColorsContent.visibility = View.GONE
            } else {
                collapsibleEditColorsContent.visibility = View.VISIBLE
            }
            isEditColorsContentVisible = !isEditColorsContentVisible
        }

        toggleThemes.setOnClickListener {
            if (isThemeLayoutVisible) {
                collapsibleThemes.visibility = View.GONE
            } else {
                collapsibleThemes.visibility = View.VISIBLE
            }
            isThemeLayoutVisible = !isThemeLayoutVisible
        }

        colorPrimaryLabel.setOnClickListener {
            primaryColorPickerDialog()
        }

        colorBackgroundLabel.setOnClickListener {
            backgroundColorPickerDialog()
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        hellThemeButton.setOnClickListener {
            primaryColorHex = "#950101"
            backgroundColorHex = "#000000"

            // Parse colors to ensure they are valid
            Color.parseColor(primaryColorHex)
            Color.parseColor(backgroundColorHex)

            // Save to SharedPreferences
            with(prefs.edit()) {
                putString("colorPrimary", primaryColorHex)
                putString("colorBackground", backgroundColorHex)
                apply()
            }

            // Apply the new colors
            updateColors(primaryColorHex, backgroundColorHex)
        }

        forestThemeButton.setOnClickListener {
            primaryColorHex = "#DAD7CD"
            backgroundColorHex = "#588157"

            // Parse colors to ensure they are valid
            Color.parseColor(primaryColorHex)
            Color.parseColor(backgroundColorHex)

            // Save to SharedPreferences
            with(prefs.edit()) {
                putString("colorPrimary", primaryColorHex)
                putString("colorBackground", backgroundColorHex)
                apply()
            }

            // Apply the new colors
            updateColors(primaryColorHex, backgroundColorHex)
        }

        darculaThemeButton.setOnClickListener {
            primaryColorHex = "#FF80BF"
            backgroundColorHex = "#21222c"

            // Parse colors to ensure they are valid
            Color.parseColor(primaryColorHex)
            Color.parseColor(backgroundColorHex)

            // Save to SharedPreferences
            with(prefs.edit()) {
                putString("colorPrimary", primaryColorHex)
                putString("colorBackground", backgroundColorHex)
                apply()
            }

            // Apply the new colors
            updateColors(primaryColorHex, backgroundColorHex)
        }

        heavenThemeButton.setOnClickListener {
            primaryColorHex = "#80BFFF"
            backgroundColorHex = "#EEEEEE"

            // Parse colors to ensure they are valid
            Color.parseColor(primaryColorHex)
            Color.parseColor(backgroundColorHex)

            // Save to SharedPreferences
            with(prefs.edit()) {
                putString("colorPrimary", primaryColorHex)
                putString("colorBackground", backgroundColorHex)
                apply()
            }

            // Apply the new colors
            updateColors(primaryColorHex, backgroundColorHex)
        }

        ikeaThemeButton.setOnClickListener {
            primaryColorHex = "#FBD914"
            backgroundColorHex = "#0058AB"

            // Parse colors to ensure they are valid
            Color.parseColor(primaryColorHex)
            Color.parseColor(backgroundColorHex)

            // Save to SharedPreferences
            with(prefs.edit()) {
                putString("colorPrimary", primaryColorHex)
                putString("colorBackground", backgroundColorHex)
                apply()
            }

            // Apply the new colors
            updateColors(primaryColorHex, backgroundColorHex)
        }

        berryThemeButton.setOnClickListener {
            primaryColorHex = "#17153B"
            backgroundColorHex = "#C8ACD6"

            // Parse colors to ensure they are valid
            Color.parseColor(primaryColorHex)
            Color.parseColor(backgroundColorHex)

            // Save to SharedPreferences
            with(prefs.edit()) {
                putString("colorPrimary", primaryColorHex)
                putString("colorBackground", backgroundColorHex)
                apply()
            }

            // Apply the new colors
            updateColors(primaryColorHex, backgroundColorHex)
        }
    }

    private fun updateColors(primaryColorHex : String, backgroundColorHex : String) {
        val primaryColor = Color.parseColor(primaryColorHex)
        val backgroundColor = Color.parseColor(backgroundColorHex)

        layout.setBackgroundColor(backgroundColor)

        title.setTextColor(primaryColor)
        btnBack.setColorFilter(primaryColor)

        viewBar.setBackgroundColor(primaryColor)
        toggleEditColors.setTextColor(primaryColor)

        colorPrimaryLabel.setTextColor(primaryColor)
        colorBackgroundLabel.setTextColor(primaryColor)

        toggleThemes.setTextColor(primaryColor)

        hellThemeButton.setBackgroundColor(Color.parseColor("#950101"))
        hellThemeButton.setTextColor(Color.parseColor("#000000"))

        forestThemeButton.setBackgroundColor(Color.parseColor("#DAD7CD"))
        forestThemeButton.setTextColor(Color.parseColor("#588157"))

        darculaThemeButton.setBackgroundColor(Color.parseColor("#FF80BF"))
        darculaThemeButton.setTextColor(Color.parseColor("#21222c"))

        heavenThemeButton.setBackgroundColor(Color.parseColor("#80BFFF"))
        heavenThemeButton.setTextColor(Color.parseColor("#C9DABF"))

        ikeaThemeButton.setBackgroundColor(Color.parseColor("#FBD914"))
        ikeaThemeButton.setTextColor(Color.parseColor("#0058AB"))

        berryThemeButton.setBackgroundColor(Color.parseColor("#17153B"))
        berryThemeButton.setTextColor(Color.parseColor("#C8ACD6"))
    }

    private fun primaryColorPickerDialog() {
        val colorPickerDialogView = LayoutInflater.from(this).inflate(R.layout.color_picker_dialog, null)
        val layout: LinearLayout = colorPickerDialogView.findViewById(R.id.layout)
        val title: TextView = colorPickerDialogView.findViewById(R.id.title)
        val viewBar: View = colorPickerDialogView.findViewById(R.id.viewBar)

        val redTextView: TextView = colorPickerDialogView.findViewById(R.id.redEditBox)
        val greenTextView: TextView = colorPickerDialogView.findViewById(R.id.greenEditBox)
        val blueTextView: TextView = colorPickerDialogView.findViewById(R.id.blueEditBox)
        redTextView.text = Color.parseColor(primaryColorHex).red.toString()
        greenTextView.text = Color.parseColor(primaryColorHex).green.toString()
        blueTextView.text = Color.parseColor(primaryColorHex).blue.toString()

        val redSlider: com.google.android.material.slider.Slider = colorPickerDialogView.findViewById(R.id.redSlider)
        val greenSlider: com.google.android.material.slider.Slider = colorPickerDialogView.findViewById(R.id.greenSlider)
        val blueSlider: com.google.android.material.slider.Slider = colorPickerDialogView.findViewById(R.id.blueSlider)
        redSlider.value = Color.parseColor(primaryColorHex).red.toFloat()
        greenSlider.value = Color.parseColor(primaryColorHex).green.toFloat()
        blueSlider.value = Color.parseColor(primaryColorHex).blue.toFloat()

        val btnCancel = colorPickerDialogView.findViewById<Button>(R.id.btnCancel)
        val btnSave = colorPickerDialogView.findViewById<Button>(R.id.btnSave)

        title.text = "Change Primary Color"

        layout.setBackgroundColor(Color.parseColor(backgroundColorHex))
        redSlider.setBackgroundColor(Color.parseColor(backgroundColorHex))

        redSlider.setTrackInactiveTintList(ColorStateList.valueOf(Color.parseColor(backgroundColorHex)))
        greenSlider.setTrackInactiveTintList(ColorStateList.valueOf(Color.parseColor(backgroundColorHex)))
        blueSlider.setTrackInactiveTintList(ColorStateList.valueOf(Color.parseColor(backgroundColorHex)))

        btnCancel.setBackgroundColor(Color.parseColor(primaryColorHex))
        btnSave.setBackgroundColor(Color.parseColor(primaryColorHex))
        title.setTextColor(Color.parseColor(primaryColorHex))
        viewBar.setBackgroundColor(Color.parseColor(primaryColorHex))

        var currentColor = Color.rgb(0, 0, 0)

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(colorPickerDialogView)
            .create()

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            if (primaryColorHex.isEmpty() || backgroundColorHex.isEmpty()) {
                Toast.makeText(this, "Please enter valid hex codes", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                // Parse colors to ensure they are valid
                Color.parseColor(primaryColorHex)
                Color.parseColor(backgroundColorHex)

                // Save to SharedPreferences
                with(prefs.edit()) {
                    putString("colorPrimary", primaryColorHex)
                    putString("colorBackground", backgroundColorHex)
                    apply()
                }

                // Apply the new colors
                updateColors(primaryColorHex, backgroundColorHex)

                Toast.makeText(this, "Colors updated successfully", Toast.LENGTH_SHORT).show()
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, "Invalid hex code", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

        // Update currentColor as sliders change
        val updateColor = {
            val red = redSlider.value.toInt()
            val green = greenSlider.value.toInt()
            val blue = blueSlider.value.toInt()
            currentColor = Color.rgb(red, green, blue)

            btnCancel.setBackgroundColor(currentColor)
            btnSave.setBackgroundColor(currentColor)
            title.setTextColor(currentColor)
            viewBar.setBackgroundColor(currentColor)

            redTextView.text = red.toString()
            greenTextView.text = green.toString()
            blueTextView.text = blue.toString()
            primaryColorHex = String.format("#%06X", 0xFFFFFF and currentColor)
        }

        redSlider.addOnChangeListener { _, _, _ -> updateColor() }
        greenSlider.addOnChangeListener { _, _, _ -> updateColor() }
        blueSlider.addOnChangeListener { _, _, _ -> updateColor() }

        dialog.show()
    }

    private fun backgroundColorPickerDialog() {
        val colorPickerDialogView = LayoutInflater.from(this).inflate(R.layout.color_picker_dialog, null)
        val layout: LinearLayout = colorPickerDialogView.findViewById(R.id.layout)
        val viewBar: View = colorPickerDialogView.findViewById(R.id.viewBar)

        val title: TextView = colorPickerDialogView.findViewById(R.id.title)
        title.text = "Change Background Color"

        val redTextView: TextView = colorPickerDialogView.findViewById(R.id.redEditBox)
        val greenTextView: TextView = colorPickerDialogView.findViewById(R.id.greenEditBox)
        val blueTextView: TextView = colorPickerDialogView.findViewById(R.id.blueEditBox)
        redTextView.text = Color.parseColor(backgroundColorHex).red.toString()
        greenTextView.text = Color.parseColor(backgroundColorHex).green.toString()
        blueTextView.text = Color.parseColor(backgroundColorHex).blue.toString()

        val redSlider: com.google.android.material.slider.Slider = colorPickerDialogView.findViewById(R.id.redSlider)
        val greenSlider: com.google.android.material.slider.Slider = colorPickerDialogView.findViewById(R.id.greenSlider)
        val blueSlider: com.google.android.material.slider.Slider = colorPickerDialogView.findViewById(R.id.blueSlider)
        redSlider.value = Color.parseColor(backgroundColorHex).red.toFloat()
        greenSlider.value = Color.parseColor(backgroundColorHex).green.toFloat()
        blueSlider.value = Color.parseColor(backgroundColorHex).blue.toFloat()

        val btnCancel = colorPickerDialogView.findViewById<Button>(R.id.btnCancel)
        val btnSave = colorPickerDialogView.findViewById<Button>(R.id.btnSave)

        layout.setBackgroundColor(Color.parseColor(backgroundColorHex))
        redSlider.setTrackInactiveTintList(ColorStateList.valueOf(Color.parseColor(backgroundColorHex)))
        greenSlider.setTrackInactiveTintList(ColorStateList.valueOf(Color.parseColor(backgroundColorHex)))
        blueSlider.setTrackInactiveTintList(ColorStateList.valueOf(Color.parseColor(backgroundColorHex)))

        btnCancel.setBackgroundColor(Color.parseColor(primaryColorHex))
        btnSave.setBackgroundColor(Color.parseColor(primaryColorHex))
        title.setTextColor(Color.parseColor(primaryColorHex))
        viewBar.setBackgroundColor(Color.parseColor(primaryColorHex))

        var currentColor: Int

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(colorPickerDialogView)
            .create()

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            if (primaryColorHex.isEmpty() || backgroundColorHex.isEmpty()) {
                Toast.makeText(this, "Please enter valid hex codes", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                // Parse colors to ensure they are valid
                Color.parseColor(primaryColorHex)
                Color.parseColor(backgroundColorHex)

                // Save to SharedPreferences
                with(prefs.edit()) {
                    putString("colorPrimary", primaryColorHex)
                    putString("colorBackground", backgroundColorHex)
                    apply()
                }

                // Apply the new colors
                updateColors(primaryColorHex, backgroundColorHex)

                Toast.makeText(this, "Colors updated successfully", Toast.LENGTH_SHORT).show()
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, "Invalid hex code", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

        // Update currentColor as sliders change
        val updateColor = {
            val red = redSlider.value.toInt()
            val green = greenSlider.value.toInt()
            val blue = blueSlider.value.toInt()
            currentColor = Color.rgb(red, green, blue)

            layout.setBackgroundColor(currentColor)
            redSlider.setTrackInactiveTintList(ColorStateList.valueOf(currentColor))
            greenSlider.setTrackInactiveTintList(ColorStateList.valueOf(currentColor))
            blueSlider.setTrackInactiveTintList(ColorStateList.valueOf(currentColor))

            btnCancel.setTextColor(currentColor)
            btnSave.setTextColor(currentColor)

            redTextView.text = red.toString()
            greenTextView.text = green.toString()
            blueTextView.text = blue.toString()
            backgroundColorHex = String.format("#%06X", 0xFFFFFF and currentColor)
        }

        redSlider.addOnChangeListener { _, _, _ -> updateColor() }
        greenSlider.addOnChangeListener { _, _, _ -> updateColor() }
        blueSlider.addOnChangeListener { _, _, _ -> updateColor() }

        dialog.show()
    }
}