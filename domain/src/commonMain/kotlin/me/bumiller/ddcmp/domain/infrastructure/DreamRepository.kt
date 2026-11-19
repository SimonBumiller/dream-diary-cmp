package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.dream.Dream

/**
 * Typed dream repository.
 */
public interface DreamRepository : Repository<Dream> {

    public suspend fun addEmotionTo(dreamId: Long, emotionId: Long)
    public suspend fun removeEmotionFrom(dreamId: Long, emotionId: Long)
    public fun getForEmotion(emotionId: Long): Flow<List<Dream>>

    public suspend fun addPersonTo(dreamId: Long, personId: Long)
    public suspend fun removePersonFrom(dreamId: Long, personId: Long)
    public fun getForPerson(personId: Long): Flow<List<Dream>>

    public fun getForTag(tagId: Long): Flow<List<Dream>>


}