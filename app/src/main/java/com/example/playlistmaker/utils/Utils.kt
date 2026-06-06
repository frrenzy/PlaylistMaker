package com.example.playlistmaker.utils


import android.view.View
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.connectBackButton(button: View) {
    button.setOnClickListener { finish() }
}
