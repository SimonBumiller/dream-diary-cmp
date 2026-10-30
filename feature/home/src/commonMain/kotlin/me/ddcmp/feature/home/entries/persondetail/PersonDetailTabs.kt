package me.ddcmp.feature.home.entries.persondetail

import dream_diary_cmp.feature.home.generated.resources.Res
import dream_diary_cmp.feature.home.generated.resources.person_detail_tabs_dreams
import dream_diary_cmp.feature.home.generated.resources.person_detail_tabs_stats
import org.jetbrains.compose.resources.StringResource

internal enum class PersonDetailTabs(
    val index: Int,
    val textResource: StringResource
) {
    Stats(0, Res.string.person_detail_tabs_stats),
    Dreams(1, Res.string.person_detail_tabs_dreams)
}