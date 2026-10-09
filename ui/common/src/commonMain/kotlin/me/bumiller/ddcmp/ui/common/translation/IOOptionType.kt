package me.bumiller.ddcmp.ui.common.translation

import androidx.compose.runtime.Composable
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.iooption_type_legacy_description
import dream_diary_cmp.ui.common.generated.resources.iooption_type_legacy_name
import dream_diary_cmp.ui.common.generated.resources.iooption_type_normal_description
import dream_diary_cmp.ui.common.generated.resources.iooption_type_normal_name
import me.bumiller.ddcmp.ui.common.model.IOOptionType
import org.jetbrains.compose.resources.stringResource

/**
 * Name of the selected import option.
 */
internal val IOOptionType.translateName: String
    @Composable get() = when (this) {
        IOOptionType.Legacy -> Res.string.iooption_type_legacy_name
        IOOptionType.Normal -> Res.string.iooption_type_normal_name
    }.let { stringResource(it) }

/**
 * Description of the selected import option.
 */
internal val IOOptionType.translateDescription: String
    @Composable get() = when (this) {
        IOOptionType.Legacy -> Res.string.iooption_type_legacy_description
        IOOptionType.Normal -> Res.string.iooption_type_normal_description
    }.let { stringResource(it) }