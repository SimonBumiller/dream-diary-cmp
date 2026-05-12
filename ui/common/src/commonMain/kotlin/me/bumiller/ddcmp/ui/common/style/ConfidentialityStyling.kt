package me.bumiller.ddcmp.ui.common.style

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import me.bumiller.ddcmp.domain.model.Confidentiality

/**
 * Gets the appropriate icon for a given confidentiality.
 */
val Confidentiality.icon: ImageVector
    @Composable get() = when (this) {
        Confidentiality.None -> Icons.Outlined.CheckCircle
        Confidentiality.Medium -> Icons.Outlined.Security
        Confidentiality.Severe -> Icons.Outlined.Lock
    }