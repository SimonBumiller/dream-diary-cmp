package me.bumiller.ddcmp.ui.common.layout

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.cd_appbar_more
import me.bumiller.ddcmp.ui.common.LocalWindowSizeClass
import org.jetbrains.compose.resources.stringResource

data class AppBarAction(
    val label: String,
    val icon: ImageVector,
    val handler: () -> Unit,
    val enabled: Boolean = true
)

@Composable
fun RowScope.AppBarActions(
    actions: List<AppBarAction>,
    sizeClass: WindowSizeClass = LocalWindowSizeClass.current
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val maxExplicit = when (sizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 2
        else -> 4
    }

    val explicit = actions.indices.filter{ it in 0..< maxExplicit }.map(actions::get)
    val hidden = actions.indices.filter{ it >= maxExplicit }.map(actions::get)

    explicit.forEach { action ->
        IconButton(
            onClick = action.handler,
            enabled = action.enabled
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.label
            )
        }
    }

    if (hidden.isNotEmpty()) {
        IconButton(
            onClick = { menuExpanded = !menuExpanded }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(Res.string.cd_appbar_more)
            )
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            hidden.forEach { action ->
                DropdownMenuItem(
                    text = {
                        Text(action.label)
                    },
                    onClick = action.handler,
                    leadingIcon = {
                        Icon(
                            imageVector = action.icon,
                            contentDescription = action.label
                        )
                    },
                    enabled = action.enabled
                )
            }
        }
    }
}