package me.bumiller.ddcmp.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/**
 * Gets the system-colored color theme for the current platform.
 *
 * @return Null if dynamic theming is not available.
 */
@Composable
expect fun systemTheme(dark: Boolean): ColorScheme?