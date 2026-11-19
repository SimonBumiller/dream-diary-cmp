package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.emotion.Emotion

/**
 * Typed emotion repository.
 */
public interface EmotionRepository : Repository<Emotion> {

    public fun getForDream(dreamId: Long): Flow<List<Emotion>?>

}