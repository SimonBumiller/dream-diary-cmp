package me.bumiller.ddcmp.domain.infrastructure.meta

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.model.meta.Unique

/**
 * Base repository for unique objects.
 */
public interface Repository<T: Unique> {

    /**
     * Flow that holds all objects.
     */
    public val all: Flow<List<T>>

    /**
     * Flow that contains the object for the id, or none.
     */
    public fun id(id: Long): Flow<T?>

    /**
     * Saves/Updates the object.
     */
    public suspend fun save(t: T): Long

    /**
     * Deletes the object with the id.
     */
    public suspend fun delete(id: Long): Boolean

    /**
     * Deletes all entries in the repository.
     */
    public suspend fun deleteAll()

}