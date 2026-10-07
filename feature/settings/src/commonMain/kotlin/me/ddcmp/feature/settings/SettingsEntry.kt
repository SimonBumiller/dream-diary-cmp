package me.ddcmp.feature.settings

import androidx.compose.runtime.Composable
import me.ddcmp.feature.settings.entries.root.SettingsRootEntry

/**
 * Entry for the settings section of the app.
 */
@Composable
fun SettingsEntry(
    onBack: () -> Unit,
    onGoToExport: () -> Unit,
    onGoToImport: () -> Unit
) {
    SettingsRootEntry(onBack, onGoToExport, onGoToImport)
}