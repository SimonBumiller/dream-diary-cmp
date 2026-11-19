package me.bumiller.ddcmp.ui.common.translation

import androidx.compose.runtime.Composable
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.intensity_high_description
import dream_diary_cmp.ui.common.generated.resources.intensity_high_name
import dream_diary_cmp.ui.common.generated.resources.intensity_low_description
import dream_diary_cmp.ui.common.generated.resources.intensity_low_name
import dream_diary_cmp.ui.common.generated.resources.intensity_medium_description
import dream_diary_cmp.ui.common.generated.resources.intensity_medium_name
import dream_diary_cmp.ui.common.generated.resources.intensity_veryhigh_description
import dream_diary_cmp.ui.common.generated.resources.intensity_veryhigh_name
import dream_diary_cmp.ui.common.generated.resources.intensity_verylow_description
import dream_diary_cmp.ui.common.generated.resources.intensity_verylow_name
import me.bumiller.ddcmp.domain.model.dream.Intensity
import org.jetbrains.compose.resources.stringResource

public val Intensity.translateName: String
    @Composable get() = when (this) {
        Intensity.VeryLow -> Res.string.intensity_verylow_name
        Intensity.Low -> Res.string.intensity_low_name
        Intensity.Medium -> Res.string.intensity_medium_name
        Intensity.High -> Res.string.intensity_high_name
        Intensity.VeryHigh -> Res.string.intensity_veryhigh_name
    }.let { stringResource(it) }

public val Intensity.translateDescription: String
    @Composable get() = when (this) {
        Intensity.VeryLow -> Res.string.intensity_verylow_description
        Intensity.Low -> Res.string.intensity_low_description
        Intensity.Medium -> Res.string.intensity_medium_description
        Intensity.High -> Res.string.intensity_high_description
        Intensity.VeryHigh -> Res.string.intensity_veryhigh_description
    }.let { stringResource(it) }