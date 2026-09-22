package me.bumiller.ddcmp.ui.common.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.back_icon_button_cd
import org.jetbrains.compose.resources.stringResource

@Composable
fun BackIconButton(
    onBack: () -> Unit
) {
    IconButton(
        onClick = onBack
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
            contentDescription = stringResource(Res.string.back_icon_button_cd)
        )
    }
}