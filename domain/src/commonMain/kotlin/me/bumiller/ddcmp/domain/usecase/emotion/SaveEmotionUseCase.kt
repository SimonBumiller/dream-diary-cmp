package me.bumiller.ddcmp.domain.usecase.emotion

import me.bumiller.ddcmp.domain.infrastructure.EmotionRepository
import me.bumiller.ddcmp.domain.model.emotion.Emotion
import me.bumiller.ddcmp.domain.usecase.meta.UseCase
import kotlin.time.ExperimentalTime

public class SaveEmotionUseCase(
    private val repo: EmotionRepository
) : UseCase<Emotion, Emotion> {

    override suspend fun invoke(i: Emotion): Emotion {
        val id = repo.save(i)

        return i.copy(id = id)
    }

}