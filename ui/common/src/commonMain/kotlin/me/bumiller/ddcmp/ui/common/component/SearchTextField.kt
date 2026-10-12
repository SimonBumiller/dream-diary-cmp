package me.bumiller.ddcmp.ui.common.component

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <Item> SearchTextField(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    label: (@Composable () -> Unit)? = null,
    values: List<Item>,
    name: (Item) -> String,
    onSelect: (Item) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var exposed by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = exposed,
        onExpandedChange = {

        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                .onFocusChanged { state ->
                    exposed = state.hasFocus
                },
            value = query,
            onValueChange = onQueryChange,
            label = label,
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        if (exposed && values.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = exposed,
                onDismissRequest = {
                    focusManager.clearFocus()
                    exposed = false
                }
            ) {
                values.forEach { value ->
                    DropdownMenuItem(
                        onClick = {
                            onSelect(value)
                        },
                        text = { Text(name(value)) }
                    )
                }
            }

        }
    }
}