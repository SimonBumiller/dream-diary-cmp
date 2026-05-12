package me.ddcmp.feature.home.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface HomeRoutes {

    @Serializable
    data object DreamList : HomeRoutes

    @Serializable
    data object EmotionList : HomeRoutes

    @Serializable
    data class AddDream(
        val dreamId: Long? = null
    ) : HomeRoutes

}