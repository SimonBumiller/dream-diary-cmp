package me.bumiller.ddcmp.ui.common.style

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private const val DISABLED_ALPHA = 0.38F

@Composable
fun DisabledContent(disabled: Boolean = true, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(
            alpha = if(disabled) DISABLED_ALPHA else 1F
        ),
        content = content
    )
}