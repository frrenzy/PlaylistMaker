package com.example.playlistmaker.utils


import android.view.View
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.connectBackButton(id: Int) {
    findViewById<View>(id).setOnClickListener { finish() }
}