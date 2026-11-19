package me.bumiller.ddcmp.domain.infrastructure

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.dream.Tag

/**
 * Repository for tags.
 */
public interface TagRepository: Repository<Tag> {

    /**
     * Gets all tags applied to a specific dream.
     */
    public fun getForDream(dreamId: Long): Flow<List<Tag>?>

    /**
     * Gets all tags applied to a specific person.
     */
    public fun getForPerson(personId: Long): Flow<List<Tag>?>

    /**
     * Adds a dream to a tag.
     */
    public suspend fun addDreamTo(tagId: Long, dreamId: Long)

    /**
     * Adds a person to a tag.
     */
    public suspend fun addPersonTo(tagId: Long, personId: Long)

    /**
     * Removes a dream from a tag.
     */
    public suspend fun removeDreamFrom(tagId: Long, dreamId: Long)

    /**
     * Removes a person from a tag.
     */
    public suspend fun removePersonFrom(tagId: Long, personId: Long)

}