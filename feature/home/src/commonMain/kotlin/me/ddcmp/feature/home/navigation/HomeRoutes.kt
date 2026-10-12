package me.ddcmp.feature.home.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface HomeRoutes {

    @Serializable
    data object Overview : HomeRoutes

    @Serializable
    data object DreamList : HomeRoutes

    @Serializable
    data object EmotionList : HomeRoutes

    @Serializable
    data object PersonList : HomeRoutes

    @Serializable
    data object TagList : HomeRoutes

    /**
     * Navigation route for the statistics screens.
     */
    @Serializable
    data class Statistics(
        val dreamId: Long? = null
    ): HomeRoutes

    /**
     * Navigation route for the person detail screen.
     */
    @Serializable
    data class PersonDetail(
        val personId: Long
    ): HomeRoutes

    @Serializable
    data class AddPerson(
        val personId: Long? = null
    ) : HomeRoutes

    @Serializable
    data class AddTag(
        val tagId: Long? = null
    ) : HomeRoutes

    @Serializable
    data class AddDream(
        val dreamId: Long? = null
    ) : HomeRoutes

    @Serializable
    data class AddEmotion(
        val emotionId: Long? = null
    ) : HomeRoutes

    @Serializable
    data class DreamDetail(
        val dreamId: Long
    ) : HomeRoutes

}