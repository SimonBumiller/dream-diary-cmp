package me.bumiller.ddcmp.domain.usecase.emotion

import kotlinx.coroutines.flow.first
import me.bumiller.ddcmp.domain.infrastructure.EmotionRepository
import me.bumiller.ddcmp.domain.model.emotion.Emotion
import me.bumiller.ddcmp.domain.usecase.meta.UseCase

/**
 * Use case for deleting an emotion.
 */
class DeleteEmotionUseCase(
    private val getEmotion: AllEmotionsUseCase,
    private val emotionRepo: EmotionRepository
) : UseCase<Long, Emotion?> {

    override suspend fun invoke(i: Long): Emotion? {
        val query = AllEmotionsUseCase.I(id = i)
        val emotion = getEmotion(query).first().firstOrNull() ?: return null

        return if (emotionRepo.delete(i)) emotion
        else null
    }

}