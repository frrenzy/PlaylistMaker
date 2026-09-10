package com.example.playlistmaker.utils

import android.content.Context
import android.os.Environment
import com.example.playlistmaker.common.data.Constants
import java.io.File

fun getCoverImageFile(context: Context, name: String?): File? = name?.let {
    val filePath =
        File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            Constants.COVERS_DIR,
        )

    if (!filePath.exists()) {
        filePath.mkdirs()
    }

    File(filePath, it)
}
