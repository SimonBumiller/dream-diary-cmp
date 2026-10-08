package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.dream.Dream

/**
 * Typed dream repository.
 */
interface DreamRepository : Repository<Dream> {

    suspend fun addEmotionTo(dreamId: Long, emotionId: Long)
    suspend fun removeEmotionFrom(dreamId: Long, emotionId: Long)
    fun getForEmotion(emotionId: Long): Flow<List<Dream>>

    suspend fun addPersonTo(dreamId: Long, personId: Long)
    suspend fun removePersonFrom(dreamId: Long, personId: Long)
    fun getForPerson(personId: Long): Flow<List<Dream>>


}