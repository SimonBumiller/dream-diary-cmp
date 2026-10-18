package me.bumiller.ddcmp.domain.model.attachment

import me.bumiller.ddcmp.domain.model.meta.Editable
import me.bumiller.ddcmp.domain.model.meta.Trackable
import me.bumiller.ddcmp.domain.model.meta.Unique
import kotlin.time.Instant

/**
 * Models attachment metadata for an attachment, usually attached to a dream.
 */
data class AttachmentMeta(

    override val id: Long,
    override val created: Instant,
    override var edited: Instant,

    /**
     * The filename the user picked.
     */
    val originalFilename: String,

    /**
     * The mimetype provided by the system.
     */
    val mimeType: String,

    /**
     * Optional description of the attachment.
     */
    val description: String?,

    /**
     * Number of bytes the file occupies.
     */
    val fileSize: Long,

    /**
     * The id of the dream it belongs to.
     */
    val dreamId: Long


) : Unique, Trackable, Editable
