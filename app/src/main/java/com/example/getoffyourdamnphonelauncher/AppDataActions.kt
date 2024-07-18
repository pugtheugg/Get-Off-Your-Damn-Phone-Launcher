package com.example.getoffyourdamnphonelauncher

import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AppDataActions(packageManager: PackageManager)  {

    private val pm = packageManager

    private val appList = MutableLiveData<MutableList<AppData>>(mutableListOf())

     fun printData() {
        for (app in appList.value!!)
            Log.i("All Apps", app.name)

        for (app in appList.value!!)
            if (app.favorite)
                Log.i("Favorite App", app.name)
    }

    fun getAllApps(): MutableList<AppData> {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfos = pm.queryIntentActivities(intent, 0)
        val appInfoList = mutableListOf<AppData>()

        for (resolveInfo in resolveInfos) {
            val appInfo = AppData(
                name = resolveInfo.loadLabel(pm).toString(),
                originalName = resolveInfo.loadLabel(pm).toString(),
                packageName = resolveInfo.activityInfo.packageName,
                favoritePosition = -1,
                favorite = false,
                folderId = -1
            )
            appInfoList.add(appInfo)
        }

        appInfoList.sortBy { it.name }

        appList.value = appInfoList
        return appList.value!!
    }


    fun addAppAsFavorite(app: AppData) {
        if (!appList.value?.contains(app)!!) return

        val element = appList.value?.indexOf(app)

        app.favorite = true
        app.favoritePosition = numberOfFavoriteApps()

        appList.value?.set(element!!, app)
        //printData()
    }

    fun removeAppAsFavorite(app: AppData) {
        if (!appList.value?.contains(app)!!) return

        val element = appList.value?.indexOf(app)

        app.favorite = false
        app.favoritePosition = -1

        appList.value?.set(element!!, app)
    }

    fun isAppFavorite(app: AppData): Boolean {
        if (!appList.value?.contains(app)!!) return false

        for (appInList in appList.value!!) {
            if (app == appInList)
                if (appInList.favorite) return true
        }
        return false
    }

    private fun numberOfFavoriteApps(): Int {
        var counter = 0

        for (app in appList.value!!) {
            if (app.favorite) counter++
        }

        return counter
    }

}