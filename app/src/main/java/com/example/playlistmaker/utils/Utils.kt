package com.example.playlistmaker.utils


import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.connectBackButton(@IdRes id: Int) {
    findViewById<View>(id).setOnClickListener { finish() }
}
