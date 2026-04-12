package me.bumiller.ddcmp.domain.infrastructure.meta

import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcmp.domain.model.meta.Unique

/**
 * Base repository for unique objects.
 */
interface Repository<T: Unique> {

    /**
     * Flow that holds all objects.
     */
    val all: Flow<List<T>>

    /**
     * Flow that contains the object for the id, or none.
     */
    fun id(id: Long): Flow<T?>

    /**
     * Saves/Updates the object.
     */
    suspend fun save(t: T): Long

    /**
     * Deletes the object with the id.
     */
    suspend fun delete(id: Long): Boolean

}