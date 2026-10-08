package me.bumiller.ddcmp.ui.common.directory

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile

expect class DirectoryPickerResultLauncher {
    fun launch()
}

@Composable
expect fun rememberPersistableDirectoryPickerLauncher(
    onResult: (PlatformFile?) -> Unit
): DirectoryPickerResultLauncher