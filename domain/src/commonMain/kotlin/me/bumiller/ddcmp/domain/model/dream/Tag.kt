package me.bumiller.ddcmp.domain.model.dream

import me.bumiller.ddcmp.domain.model.meta.Editable
import me.bumiller.ddcmp.domain.model.meta.Trackable
import me.bumiller.ddcmp.domain.model.meta.Unique
import kotlin.time.Instant

/**
 * Models a tag applied to a [Dream] or [Person].
 *
 * This can be thought of as a property, attribute or group that the applied model has.
 */
data class Tag(

    override val id: Long,
    override val created: Instant,
    override var edited: Instant,

    /**
     * The name of the tag.
     */
    val name: String,

    /**
     * Further description of the tag.
     */
    val description: String?,

    /**
     * The color in argb mode.
     */
    val color: Long

): Unique, Trackable, Editable
