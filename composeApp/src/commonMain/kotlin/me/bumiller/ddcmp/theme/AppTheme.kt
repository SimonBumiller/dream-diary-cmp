package me.bumiller.ddcmp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import me.bumiller.ddcmp.domain.model.settings.ColorMode
import me.bumiller.ddcmp.domain.model.settings.ColorTheme
import me.bumiller.ddcmp.ui.common.LocalWindowSizeClass

/**
 * The material theme for the app.
 *
 * Takes configuration parameters to create the material setup.
 */
@Composable
fun AppTheme(
    mode: ColorMode,
    theme: ColorTheme,
    sizeClass: WindowSizeClass,
    systemTheme: @Composable (dark: Boolean) -> ColorScheme?,
    content: @Composable () -> Unit
) {
    val doDark = when (mode) {
        ColorMode.Light -> false
        ColorMode.Dark -> true
        ColorMode.System -> isSystemInDarkTheme()
    }

    val staticTheme = if(doDark) AppDarkColorScheme
        else AppLightColorScheme

    val colorScheme = when (theme) {
        ColorTheme.System -> systemTheme(doDark) ?: staticTheme
        else -> staticTheme
    }

    CompositionLocalProvider(
        LocalWindowSizeClass provides sizeClass
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }

}