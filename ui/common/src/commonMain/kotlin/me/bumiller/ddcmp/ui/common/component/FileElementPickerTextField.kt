package me.bumiller.ddcmp.ui.common.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.path
import me.bumiller.ddcmp.ui.common.directory.rememberPersistableDirectoryPickerLauncher

/**
 * Component that allows a user to pick a persistable directory path.
 */
@Composable
fun PersistableDirectoryTextField(
    modifier: Modifier,
    file: PlatformFile?,
    onFileChanged: (PlatformFile?) -> Unit,
    placeholder: String
) {
    val launcher = rememberPersistableDirectoryPickerLauncher(onFileChanged)

    FileElementPickerTextField(
        modifier = modifier,
        onLaunch = launcher::launch,
        value = file?.path ?: "",
        placeholder = placeholder
    )
}

/**
 * Component that allows a user to pick a new file.
 */
@Composable
fun FileSaverTextField(
    modifier: Modifier,
    file: PlatformFile?,
    onFileChanged: (PlatformFile?) -> Unit,
    suggestedName: String,
    extension: String,
    placeholder: String
) {
    val launcher = rememberFileSaverLauncher(onResult = onFileChanged)

    FileElementPickerTextField(
        modifier = modifier,
        onLaunch = { launcher.launch(suggestedName, extension) },
        value = file?.path ?: "",
        placeholder = placeholder
    )
}

/**
 * Component that allows a user to pick a new file.
 */
@Composable
fun FileSelectorTextField(
    modifier: Modifier,
    file: PlatformFile?,
    onFileChanged: (PlatformFile?) -> Unit,
    extension: String,
    placeholder: String
) {
    val launcher = rememberFilePickerLauncher(
        type = FileKitType.File(extension),
        onResult = onFileChanged
    )

    FileElementPickerTextField(
        modifier = modifier,
        onLaunch = launcher::launch,
        value = file?.path ?: "",
        placeholder = placeholder
    )
}

/**
 * Configured a TextField for semantic use to use together with several different launcher.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FileElementPickerTextField(
    modifier: Modifier,
    onLaunch: () -> Unit,
    value: String? = null,
    placeholder: String
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = false,
        onExpandedChange = { onLaunch() }
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(
                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                enabled = true
            ),
            value = value ?: "",
            readOnly = true,
            onValueChange = { },
            placeholder = {
                Text(placeholder)
            }
        )
    }
}