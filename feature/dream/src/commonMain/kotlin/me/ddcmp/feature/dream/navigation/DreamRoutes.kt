package me.ddcmp.feature.dream.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface DreamRoutes {

    @Serializable
    data object DreamList : DreamRoutes

    @Serializable
    data class AddDream(
        val dreamId: Long? = null
    ) : DreamRoutes

    @Serializable
    data class DreamDetail(
        val dreamId: Long
    ) : DreamRoutes

}