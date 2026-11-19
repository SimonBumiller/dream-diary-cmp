package me.bumiller.ddcmp.ui.common.directory

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile

public expect class DirectoryPickerResultLauncher {
    public fun launch()
}

@Composable
public expect fun rememberPersistableDirectoryPickerLauncher(
    onResult: (PlatformFile?) -> Unit
): DirectoryPickerResultLauncher