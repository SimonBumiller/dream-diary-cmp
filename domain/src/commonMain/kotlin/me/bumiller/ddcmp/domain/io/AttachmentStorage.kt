package me.bumiller.ddcmp.domain.io

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.io.RawSource
import kotlinx.io.Source

/**
 * Storage abstraction for attachments.
 */
public interface AttachmentStorage {

    /**
     * Saves the bytes from a source into persisted storage
     *
     * @param id The id of the attachment
     * @param source Where the bytes are taken from
     * @return The total byte count
     */
    public suspend fun save(id: Long, source: RawSource): Long

    /**
     * Deletes the bytes from the storage.
     */
    public suspend fun delete(id: Long): Boolean

    /**
     * Gets the source for an attachment for a specific id
     *
     * @return The source, or null if not found
     */
    public suspend fun get(id: Long): PlatformFile?

    public suspend fun deleteAll(): Int

}