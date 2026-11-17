package me.ddcmp.feature.emotion.navigation

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface EmotionRoutes {

    @Serializable
    data object EmotionList : EmotionRoutes

    @Serializable
    data class AddEmotion(
        val emotionId: Long? = null
    ) : EmotionRoutes

}