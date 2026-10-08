package me.bumiller.ddcmp.ui.common.translation

import androidx.compose.runtime.Composable
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.colormode_dark_name
import dream_diary_cmp.ui.common.generated.resources.colormode_light_name
import dream_diary_cmp.ui.common.generated.resources.colormode_system_name
import me.bumiller.ddcmp.domain.model.settings.ColorMode
import org.jetbrains.compose.resources.stringResource

/**
 * The translated description of the confidentiality.
 */
val ColorMode.translateName: String
    @Composable get() = when (this) {
        ColorMode.Light -> Res.string.colormode_light_name
        ColorMode.Dark -> Res.string.colormode_dark_name
        ColorMode.System -> Res.string.colormode_system_name
    }.let { stringResource(it) }