package me.bumiller.ddcmp.ui.common.translation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AudioFile
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.VideoFile
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun String.mimeTypeIcon(): ImageVector = when {
    "image/" in this -> Icons.Outlined.Image
    "video/" in this -> Icons.Outlined.VideoFile
    "audio/" in this -> Icons.Outlined.AudioFile
    else -> Icons.Outlined.Description
}