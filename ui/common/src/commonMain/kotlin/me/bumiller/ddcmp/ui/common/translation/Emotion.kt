package me.bumiller.ddcmp.ui.common.translation

import androidx.compose.runtime.Composable
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.emotiontype_anger
import dream_diary_cmp.ui.common.generated.resources.emotiontype_joy
import dream_diary_cmp.ui.common.generated.resources.emotiontype_pleasure
import dream_diary_cmp.ui.common.generated.resources.emotiontype_sadness
import me.bumiller.ddcmp.domain.model.emotion.EmotionType
import org.jetbrains.compose.resources.stringResource

/**
 * Translated name of the emotion type.
 */
val EmotionType.translateName: String
    @Composable get() = when (this) {
        EmotionType.Joy -> stringResource(Res.string.emotiontype_joy)
        EmotionType.Pleasure -> stringResource(Res.string.emotiontype_pleasure)
        EmotionType.Sadness -> stringResource(Res.string.emotiontype_sadness)
        EmotionType.Anger -> stringResource(Res.string.emotiontype_anger)
    }