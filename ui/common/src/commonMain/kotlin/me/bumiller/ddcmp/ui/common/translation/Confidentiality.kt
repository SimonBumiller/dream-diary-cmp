package me.bumiller.ddcmp.ui.common.translation

import androidx.compose.runtime.Composable
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.confidentiality_medium_description
import dream_diary_cmp.ui.common.generated.resources.confidentiality_medium_name
import dream_diary_cmp.ui.common.generated.resources.confidentiality_none_description
import dream_diary_cmp.ui.common.generated.resources.confidentiality_none_name
import dream_diary_cmp.ui.common.generated.resources.confidentiality_severe_description
import dream_diary_cmp.ui.common.generated.resources.confidentiality_severe_name
import me.bumiller.ddcmp.domain.model.dream.Confidentiality
import org.jetbrains.compose.resources.stringResource

/**
 * The translated name of the confidentiality.
 */
val Confidentiality.translateName: String
    @Composable get() = when (this) {
        Confidentiality.None -> Res.string.confidentiality_none_name
        Confidentiality.Medium -> Res.string.confidentiality_medium_name
        Confidentiality.Severe -> Res.string.confidentiality_severe_name
    }.let { stringResource(it) }

/**
 * The translated description of the confidentiality.
 */
val Confidentiality.translateDescription: String
    @Composable get() = when (this) {
        Confidentiality.None -> Res.string.confidentiality_none_description
        Confidentiality.Medium -> Res.string.confidentiality_medium_description
        Confidentiality.Severe -> Res.string.confidentiality_severe_description
    }.let { stringResource(it) }