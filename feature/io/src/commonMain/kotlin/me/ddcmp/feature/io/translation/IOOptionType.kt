package me.ddcmp.feature.io.translation

import androidx.compose.runtime.Composable
import dream_diary_cmp.feature.io.generated.resources.Res
import dream_diary_cmp.feature.io.generated.resources.importoption_legacy_description
import dream_diary_cmp.feature.io.generated.resources.importoption_legacy_name
import dream_diary_cmp.feature.io.generated.resources.importoption_normal_description
import dream_diary_cmp.feature.io.generated.resources.importoption_normal_name
import me.ddcmp.feature.io.model.IOOptionType
import org.jetbrains.compose.resources.stringResource

/**
 * Name of the selected import option.
 */
internal val IOOptionType.translateName: String
    @Composable get() = when (this) {
        IOOptionType.Legacy -> Res.string.importoption_legacy_name
        IOOptionType.Normal -> Res.string.importoption_normal_name
    }.let { stringResource(it) }

/**
 * Description of the selected import option.
 */
internal val IOOptionType.translateDescription: String
    @Composable get() = when (this) {
        IOOptionType.Legacy -> Res.string.importoption_legacy_description
        IOOptionType.Normal -> Res.string.importoption_normal_description
    }.let { stringResource(it) }