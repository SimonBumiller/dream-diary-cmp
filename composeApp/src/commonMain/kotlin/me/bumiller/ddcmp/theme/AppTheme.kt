package me.bumiller.ddcmp.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import me.bumiller.ddcmp.domain.model.ColorMode
import me.bumiller.ddcmp.domain.model.ColorTheme

/**
 * The material theme for the app.
 *
 * Takes configuration parameters to create the material setup.
 */
@Composable
fun AppTheme(
    mode: ColorMode,
    theme: ColorTheme,
    systemTheme: @Composable (ColorMode) -> ColorScheme?,
    content: @Composable () -> Unit
) {
    val staticTheme = when (mode) {
        ColorMode.Dark -> AppDarkColorScheme
        else -> AppLightColorScheme
    }

    val colorScheme = when (theme) {
        ColorTheme.System -> systemTheme(mode) ?: staticTheme
        else -> staticTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}