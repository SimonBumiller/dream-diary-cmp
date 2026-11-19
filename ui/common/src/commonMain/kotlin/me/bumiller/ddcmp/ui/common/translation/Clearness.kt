package me.bumiller.ddcmp.ui.common.translation

import androidx.compose.runtime.Composable
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.clearness_high_description
import dream_diary_cmp.ui.common.generated.resources.clearness_high_name
import dream_diary_cmp.ui.common.generated.resources.clearness_low_description
import dream_diary_cmp.ui.common.generated.resources.clearness_low_name
import dream_diary_cmp.ui.common.generated.resources.clearness_medium_description
import dream_diary_cmp.ui.common.generated.resources.clearness_medium_name
import dream_diary_cmp.ui.common.generated.resources.clearness_veryhigh_description
import dream_diary_cmp.ui.common.generated.resources.clearness_veryhigh_name
import dream_diary_cmp.ui.common.generated.resources.clearness_verylow_description
import dream_diary_cmp.ui.common.generated.resources.clearness_verylow_name
import me.bumiller.ddcmp.domain.model.dream.Clearness
import org.jetbrains.compose.resources.stringResource

public val Clearness.translateName: String
    @Composable get() = when (this) {
        Clearness.VeryLow -> Res.string.clearness_verylow_name
        Clearness.Low -> Res.string.clearness_low_name
        Clearness.Medium -> Res.string.clearness_medium_name
        Clearness.High -> Res.string.clearness_high_name
        Clearness.VeryHigh -> Res.string.clearness_veryhigh_name
    }.let { stringResource(it) }

public val Clearness.translateDescription: String
    @Composable get() = when (this) {
        Clearness.VeryLow -> Res.string.clearness_verylow_description
        Clearness.Low -> Res.string.clearness_low_description
        Clearness.Medium -> Res.string.clearness_medium_description
        Clearness.High -> Res.string.clearness_high_description
        Clearness.VeryHigh -> Res.string.clearness_veryhigh_description
    }.let { stringResource(it) }