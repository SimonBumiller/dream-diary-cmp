package me.bumiller.ddcmp.ui.common.translation

import androidx.compose.runtime.Composable
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.sort_ascending
import dream_diary_cmp.ui.common.generated.resources.sort_created
import dream_diary_cmp.ui.common.generated.resources.sort_descending
import dream_diary_cmp.ui.common.generated.resources.sort_dream_confidentiality
import dream_diary_cmp.ui.common.generated.resources.sort_dream_dreamt
import dream_diary_cmp.ui.common.generated.resources.sort_dream_length
import dream_diary_cmp.ui.common.generated.resources.sort_dream_name
import dream_diary_cmp.ui.common.generated.resources.sort_dream_valence
import dream_diary_cmp.ui.common.generated.resources.sort_emotion_name
import dream_diary_cmp.ui.common.generated.resources.sort_person_name
import dream_diary_cmp.ui.common.generated.resources.sort_updated
import me.bumiller.ddcmp.domain.model.meta.SortDirection
import me.bumiller.ddcmp.domain.model.meta.SortMode
import org.jetbrains.compose.resources.stringResource

/**
 * Gets the translated name for the specific sort mode.
 */
val SortMode.translateName: String
    @Composable get() = when (this) {
        SortMode.Created -> Res.string.sort_created
        SortMode.DreamSort.AverageValence -> Res.string.sort_dream_valence
        SortMode.DreamSort.Confidentiality -> Res.string.sort_dream_confidentiality
        SortMode.DreamSort.Dreamt -> Res.string.sort_dream_dreamt
        SortMode.DreamSort.Name -> Res.string.sort_dream_name
        SortMode.EmotionSort.Name -> Res.string.sort_emotion_name
        SortMode.PersonSort.Name -> Res.string.sort_person_name
        SortMode.Updated -> Res.string.sort_updated
        SortMode.DreamSort.Length -> Res.string.sort_dream_length
    }.let { stringResource(it) }

/**
 * Gets the translated name for the specific sort direction.
 */
val SortDirection.translateName: String
    @Composable get() = when (this) {
        SortDirection.Ascending -> Res.string.sort_ascending
        SortDirection.Descending -> Res.string.sort_descending
    }.let { stringResource(it) }