package me.bumiller.ddcmp.ui.common.directory

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile

actual class DirectoryPickerResultLauncher {
    actual fun launch() {}
}

@Composable
actual fun rememberPersistableDirectoryPickerLauncher(onResult: (PlatformFile?) -> Unit): DirectoryPickerResultLauncher {
    return DirectoryPickerResultLauncher()
}