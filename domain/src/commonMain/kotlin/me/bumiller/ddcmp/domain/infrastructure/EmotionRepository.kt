package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.meta.SortMode

/**
 * Typed emotion repository.
 */
interface EmotionRepository : Repository<Emotion> {

    fun getForDream(dreamId: Long): Flow<List<Emotion>?>

}