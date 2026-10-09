package me.bumiller.ddcmp.ui.common.component

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.io_file_selector_label
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.ui.common.translation.translateName
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IOFileSelector(
    showDropdown: Boolean,
    toggleDropdownVisibility: () -> Unit,
    onTypeSelected: (IOFileType) -> Unit,
    fileType: IOFileType
) {
    ExposedDropdownMenuBox(
        expanded = showDropdown,
        onExpandedChange = { toggleDropdownVisibility() }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
            value = fileType.translateName,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(Res.string.io_file_selector_label)) }
        )
        ExposedDropdownMenu(
            expanded = showDropdown,
            onDismissRequest = toggleDropdownVisibility
        ) {
            IOFileType.entries.forEach { filetype ->
                DropdownMenuItem(
                    onClick = {
                        onTypeSelected(filetype)
                        toggleDropdownVisibility()
                    },
                    text = { Text(filetype.translateName) }
                )
            }
        }
    }
}