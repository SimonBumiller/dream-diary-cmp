package me.ddcmp.feature.import.translation

import androidx.compose.runtime.Composable
import dream_diary_cmp.feature.import.generated.resources.Res
import dream_diary_cmp.feature.import.generated.resources.importoption_legacy_description
import dream_diary_cmp.feature.import.generated.resources.importoption_legacy_name
import dream_diary_cmp.feature.import.generated.resources.importoption_normal_description
import dream_diary_cmp.feature.import.generated.resources.importoption_normal_name
import me.ddcmp.feature.import.model.ImportOptionType
import org.jetbrains.compose.resources.stringResource

/**
 * Name of the selected import option.
 */
internal val ImportOptionType.translateName: String
    @Composable get() = when (this) {
        ImportOptionType.Legacy -> Res.string.importoption_legacy_name
        ImportOptionType.Normal -> Res.string.importoption_normal_name
    }.let { stringResource(it) }

/**
 * Description of the selected import option.
 */
internal val ImportOptionType.translateDescription: String
    @Composable get() = when (this) {
        ImportOptionType.Legacy -> Res.string.importoption_legacy_description
        ImportOptionType.Normal -> Res.string.importoption_normal_description
    }.let { stringResource(it) }