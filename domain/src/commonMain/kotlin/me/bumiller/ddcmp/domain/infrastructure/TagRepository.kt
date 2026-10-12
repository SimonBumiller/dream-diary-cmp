package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.dream.Tag

/**
 * Repository for tags.
 */
interface TagRepository: Repository<Tag> {

    /**
     * Gets all tags applied to a specific dream.
     */
    fun getForDream(dreamId: Long): Flow<List<Tag>?>

    /**
     * Gets all tags applied to a specific person.
     */
    fun getForPerson(personId: Long): Flow<List<Tag>?>

    /**
     * Adds a dream to a tag.
     */
    suspend fun addDreamTo(tagId: Long, dreamId: Long)

    /**
     * Adds a person to a tag.
     */
    suspend fun addPersonTo(tagId: Long, personId: Long)

    /**
     * Removes a dream from a tag.
     */
    suspend fun removeDreamFrom(tagId: Long, dreamId: Long)

    /**
     * Removes a person from a tag.
     */
    suspend fun removePersonFrom(tagId: Long, personId: Long)

}