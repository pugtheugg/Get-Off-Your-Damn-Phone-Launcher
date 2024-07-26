package com.example.getoffyourdamnphonelauncher

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: ScreenSlidePagerAdapter
    private lateinit var appDataActions: AppDataActions
    private lateinit var sharedAppData: SharedAppData

    private lateinit var prefs: SharedPreferences

    private var primaryColorHex: String = ""
    private var backgroundColorHex: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.view_pager)
        pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        appDataActions = AppDataActions(packageManager)
        sharedAppData = ViewModelProvider(this)[SharedAppData::class.java]

        // Set initial data in ViewModel
        if (sharedAppData.getAppList().isEmpty()) {
            sharedAppData.setAppList(sharedAppData.loadAppList())
        }

        if (sharedAppData.getAppList().isEmpty()) {
            sharedAppData.setAppList(appDataActions.getAllApps())
        }

        prefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        val defaultPrimaryColor = String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(this, R.color.colorPrimary))
        val defaultBackgroundColor = String.format("#%06X", 0xFFFFFF and ContextCompat.getColor(this, R.color.colorBackground))

        primaryColorHex = prefs.getString("colorPrimary", defaultPrimaryColor) ?: defaultPrimaryColor
        backgroundColorHex = prefs.getString("colorBackground", defaultBackgroundColor) ?: defaultBackgroundColor
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment(primaryColorHex, backgroundColorHex)
                1 -> AllAppsFragment(primaryColorHex, backgroundColorHex)
                else -> HomeFragment(primaryColorHex, backgroundColorHex)
            }
        }
    }
}