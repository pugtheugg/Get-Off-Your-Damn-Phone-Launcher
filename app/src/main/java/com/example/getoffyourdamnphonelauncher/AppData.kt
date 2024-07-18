package com.example.getoffyourdamnphonelauncher

import java.io.Serializable

data class AppData(
    var name: String,
    val originalName: String,
    var packageName: String,
    var folderId: Int,
    var favoritePosition: Int,
    var favorite: Boolean,
)
