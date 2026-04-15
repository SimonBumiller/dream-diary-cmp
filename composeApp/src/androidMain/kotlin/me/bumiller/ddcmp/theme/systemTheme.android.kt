package me.bumiller.ddcmp.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun systemTheme(dark: Boolean): ColorScheme? {
    val context = LocalContext.current

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return null

    return if (dark) dynamicDarkColorScheme(context)
    else dynamicLightColorScheme(context)
}