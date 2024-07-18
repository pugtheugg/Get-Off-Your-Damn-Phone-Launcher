package com.example.getoffyourdamnphonelauncher

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedAppData(application: Application) : AndroidViewModel(application)  {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _appList = MutableLiveData<MutableList<AppData>>(mutableListOf())
    val apps: LiveData<MutableList<AppData>> = _appList

    private fun saveAppList(appList: MutableList<AppData>) {
        val jsonString = gson.toJson(appList)
        sharedPreferences.edit().putString("app_list", jsonString).apply()
    }

     fun loadAppList(): MutableList<AppData> {
        val jsonString = sharedPreferences.getString("app_list", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<AppData>>() {}.type
        return gson.fromJson(jsonString, type) ?: mutableListOf()
    }

    fun printData() {
        for (app in apps.value!!)
            Log.i("All Apps", app.name)

        for (app in apps.value!!) {
            if (app.favorite) {
                Log.i("Favorite App", app.name)
                Log.i("App Position", app.favoritePosition.toString())
            }
        }
    }

    fun setAppList(apps: MutableList<AppData>) {
        _appList.value = apps
        saveAppList(apps)
    }

    fun addApp(app: AppData) {
        _appList.value?.add(app)
        _appList.value = _appList.value // trigger observers
        saveAppList(_appList.value!!)
    }

    fun getAppList(): MutableList<AppData> {
        return apps.value!!
    }

    fun addAppAsFavorite(app: AppData) {
        val index = _appList.value?.indexOf(app)

        if (index != null && index != -1) {
            app.favorite = true
            app.favoritePosition = getLatestFavoritePosition()
            _appList.value?.set(index, app)
            _appList.value = _appList.value
            saveAppList(_appList.value!!)
        }
    }

    fun removeAppAsFavorite(app: AppData) {
        val index = _appList.value?.indexOf(app)

        if (index != null && index != -1) {
            app.favorite = false
            app.favoritePosition = -1
            _appList.value?.set(index, app)
            _appList.value = _appList.value
            saveAppList(_appList.value!!)
        }
    }

    fun isAppFavorite(app: AppData): Boolean {
        return _appList.value?.contains(app) == true && app.favorite
    }

    fun renameApp(app: AppData, newName: String) {
        val index = _appList.value?.indexOf(app)

        if (index != null && index != -1) {
            app.name = newName
            _appList.value?.set(index, app)
            _appList.value = _appList.value
            saveAppList(_appList.value!!)
        }
    }

    fun favoriteAppDown(app: AppData) {
        val appList = _appList.value ?: return
        val targetValue = app.favoritePosition

        // Find the closest higher value app
        var closestApp: AppData? = null
        var smallestDifference = Int.MAX_VALUE

        for (otherApp in appList) {
            if (otherApp.favorite && otherApp.favoritePosition > targetValue) {
                val difference = otherApp.favoritePosition - targetValue
                if (difference < smallestDifference) {
                    smallestDifference = difference
                    closestApp = otherApp
                }
            }
        }

        // If a closest app is found, swap their favorite positions
        if (closestApp != null) {
            val tempPosition = app.favoritePosition
            app.favoritePosition = closestApp.favoritePosition
            closestApp.favoritePosition = tempPosition

            // Update the list and save changes
            val appIndex = appList.indexOf(app)
            val closestAppIndex = appList.indexOf(closestApp)
            if (appIndex != -1 && closestAppIndex != -1) {
                appList[appIndex] = app
                appList[closestAppIndex] = closestApp
                _appList.value = appList
                saveAppList(appList)
            }
        }
    }

    fun favoriteAppUp(app: AppData) {
        val appList = _appList.value ?: return
        val targetValue = app.favoritePosition

        // Find the closest lower value app
        var closestApp: AppData? = null
        var smallestDifference = Int.MAX_VALUE

        for (otherApp in appList) {
            if (otherApp.favorite && otherApp.favoritePosition < targetValue) {
                val difference = targetValue - otherApp.favoritePosition
                if (difference < smallestDifference) {
                    smallestDifference = difference
                    closestApp = otherApp
                }
            }
        }

        // If a closest app is found, swap their favorite positions
        if (closestApp != null) {
            val tempPosition = app.favoritePosition
            app.favoritePosition = closestApp.favoritePosition
            closestApp.favoritePosition = tempPosition

            // Update the list and save changes
            val appIndex = appList.indexOf(app)
            val closestAppIndex = appList.indexOf(closestApp)
            if (appIndex != -1 && closestAppIndex != -1) {
                appList[appIndex] = app
                appList[closestAppIndex] = closestApp
                _appList.value = appList
                saveAppList(appList)
            }
        }
    }

    private fun getLatestFavoritePosition(): Int {
        var highestPosition = 0

        for (app in _appList.value!!) {
            if (app.favoritePosition > highestPosition)
                highestPosition = app.favoritePosition
        }

        return highestPosition + 1
    }
}
