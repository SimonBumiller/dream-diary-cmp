package me.bumiller.ddcmp.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import me.bumiller.ddcmp.domain.model.ColorMode

@Composable
actual fun systemTheme(mode: ColorMode): ColorScheme? {
    val context = LocalContext.current

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return null

    return when (mode) {
        ColorMode.Light -> dynamicLightColorScheme(context)
        ColorMode.Dark -> dynamicDarkColorScheme(context)
        ColorMode.System -> if (isSystemInDarkTheme())
            dynamicDarkColorScheme(context)
        else
            dynamicLightColorScheme(context)
    }
}