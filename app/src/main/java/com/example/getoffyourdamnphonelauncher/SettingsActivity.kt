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

    lateinit var layout: LinearLayout
    lateinit var title: TextView
    lateinit var viewBar: View
    lateinit var colorPrimaryLabel: TextView
    lateinit var colorBackgroundLabel: TextView
    lateinit var btnBack: ImageButton
    lateinit var toggleEditColors: TextView
    lateinit var collapsibleEditColorsContent: LinearLayout

    lateinit var prefs: SharedPreferences

    private var primaryColorHex: String = ""
    private var backgroundColorHex: String = ""

    private var isEditColorsContentVisible = false

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
        collapsibleEditColorsContent = findViewById(R.id.collapsibleEditColorsContent)

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
    }

    private fun updateColors(primaryColorHex : String, backgroundColorHex : String) {
        layout.setBackgroundColor(Color.parseColor(backgroundColorHex))

        title.setTextColor(Color.parseColor(primaryColorHex))
        btnBack.setColorFilter(Color.parseColor(primaryColorHex))

        viewBar.setBackgroundColor(Color.parseColor(primaryColorHex))
        toggleEditColors.setTextColor(Color.parseColor(primaryColorHex))

        colorPrimaryLabel.setTextColor(Color.parseColor(primaryColorHex))
        colorBackgroundLabel.setTextColor(Color.parseColor(primaryColorHex))
    }

    private fun primaryColorPickerDialog() {
        val colorPickerDialogView = LayoutInflater.from(this).inflate(R.layout.color_picker_dialog, null)
        val layout: LinearLayout = colorPickerDialogView.findViewById(R.id.layout)
        val title: TextView = colorPickerDialogView.findViewById(R.id.title)
        val viewBar: View = colorPickerDialogView.findViewById(R.id.viewBar)

        val redText: TextView = colorPickerDialogView.findViewById(R.id.redEditBox)
        val greenEditBox: TextView = colorPickerDialogView.findViewById(R.id.greenEditBox)
        val blueEditBox: TextView = colorPickerDialogView.findViewById(R.id.blueEditBox)
        redText.text = Color.parseColor(primaryColorHex).red.toString()
        greenEditBox.text = Color.parseColor(primaryColorHex).green.toString()
        blueEditBox.text = Color.parseColor(primaryColorHex).blue.toString()

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

            redText.text = red.toString()
            greenEditBox.text = green.toString()
            blueEditBox.text = blue.toString()
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

        val redTextBox: TextView = colorPickerDialogView.findViewById(R.id.redEditBox)
        val greenTextBox: TextView = colorPickerDialogView.findViewById(R.id.greenEditBox)
        val blueTextBox: TextView = colorPickerDialogView.findViewById(R.id.blueEditBox)
        redTextBox.text = Color.parseColor(backgroundColorHex).red.toString()
        greenTextBox.text = Color.parseColor(backgroundColorHex).green.toString()
        blueTextBox.text = Color.parseColor(backgroundColorHex).blue.toString()

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

            redTextBox.text = red.toString()
            greenTextBox.text = green.toString()
            blueTextBox.text = blue.toString()
            backgroundColorHex = String.format("#%06X", 0xFFFFFF and currentColor)
        }

        redSlider.addOnChangeListener { _, _, _ -> updateColor() }
        greenSlider.addOnChangeListener { _, _, _ -> updateColor() }
        blueSlider.addOnChangeListener { _, _, _ -> updateColor() }

        dialog.show()
    }
}