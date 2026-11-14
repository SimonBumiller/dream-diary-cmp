package me.bumiller.ddcmp.ui.common.translation

import androidx.compose.runtime.Composable
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.tag_target_dream
import dream_diary_cmp.ui.common.generated.resources.tag_target_person
import me.bumiller.ddcmp.domain.model.dream.Tag
import org.jetbrains.compose.resources.stringResource

val Tag.TagTarget.translateName: String
    @Composable get() = when (this) {
        Tag.TagTarget.Dream -> Res.string.tag_target_dream
        Tag.TagTarget.Person -> Res.string.tag_target_person
    }.let { stringResource(it) }