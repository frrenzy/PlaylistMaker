package com.example.playlistmaker.common.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val black = Color(0xFF1A1B22)
val white = Color(0xFFFFFFFF)
val grey = Color(0xFFAEAFB4)
val blue = Color(0xFF3772E7)
val lightGrey = Color(0xFFE6E8EB)
val yellow = Color(0xFFFCD452)
val extraLightGrey = Color(0xFFC6C6C8)
val darkGrey = Color(0xFF404040)
val red = Color(0xFFF56B6C)

val lightColors = lightColorScheme(
    surface = white,
    onSurface = black,
    primary = blue,
    secondary = grey, //icon color
    tertiary = grey, //fieldIconColor
    secondaryContainer = lightGrey, //fieldBackColor
    outline = extraLightGrey, //button color
    onSecondaryContainer = black, //text field text
)

val darkColors = darkColorScheme(
    surface = black,
    onSurface = white,
    primary = blue,
    secondary = white,
    tertiary = black,
    secondaryContainer = white,
    outline = darkGrey,
    onSecondaryContainer = black,
)
