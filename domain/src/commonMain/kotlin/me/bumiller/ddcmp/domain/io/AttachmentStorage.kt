package me.bumiller.ddcmp.domain.io

import kotlinx.io.Source

/**
 * Storage abstraction for attachments.
 */
interface AttachmentStorage {

    /**
     * Saves the bytes from a source into persisted storage
     *
     * @param id The id of the attachment
     * @param source Where the bytes are taken from
     * @return The total byte count
     */
    fun save(id: Long, source: Source): Long

    /**
     * Gets the source for an attachment for a specific id
     *
     * @return The source, or null if not found
     */
    fun get(id: Long): Source?

}