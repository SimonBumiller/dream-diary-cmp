package me.bumiller.ddcmp.domain.model.dream

import me.bumiller.ddcmp.domain.model.meta.Editable
import me.bumiller.ddcmp.domain.model.meta.Searchable
import me.bumiller.ddcmp.domain.model.meta.Unique
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Models a dream that has been recorded.
 */
data class Dream(

    override val id: Long,
    override val created: Instant,
    override var edited: Instant,

    /**
     * Timestamp when the dream was dreamt.
     * May differ from [created].
     */
    var dreamt: Instant,

    /**
     * The optional title of the dream.
     */
    var title: String?,

    /**
     * The main description of the dream.
     */
    var description: String,

    /**
     * Optional meta-notes about the dream.
     */
    var note: String?,

    /**
     * Whether the dream is marked as a favorite
     */
    var favorite: Boolean,

    /**
     * The confidentiality level of the dream.
     */
    var confidentiality: Confidentiality,

    /**
     * Clearness of the dream.
     */
    var clearness: Clearness?,

    /**
     * Intensity of the dream
     */
    var intensity: Intensity?

) : Unique, Editable, Searchable {

    override val searchableFields: List<String>
        get() = listOfNotNull(title, description, note)

    val length: Int
        get() = Regex("\\S+").findAll(description).count()

}
