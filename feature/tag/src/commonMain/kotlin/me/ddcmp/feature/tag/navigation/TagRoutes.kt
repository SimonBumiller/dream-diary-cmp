package me.ddcmp.feature.tag.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface TagRoutes {

    @Serializable
    data object TagList : TagRoutes

    @Serializable
    data class AddTag(
        val tagId: Long? = null
    ) : TagRoutes

}