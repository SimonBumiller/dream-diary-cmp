package me.bumiller.ddcmp.domain.model

import me.bumiller.ddcmp.domain.model.meta.Editable
import me.bumiller.ddcmp.domain.model.meta.Unique
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Models a dream that has been recorded.
 */
@OptIn(ExperimentalTime::class)
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
     * The persons that are part of the dream.
     */
    val persons: MutableList<Person>,

    /**
     * The emotions that are part of the dream.
     */
    val emotions: MutableList<Emotion>

) : Unique, Editable {

    /**
     * Dynamically calculates the average valence of the dream's emotions.
     */
    val averageValence: Valence
        get() = Valence(
            emotions
                .map(Emotion::valence)
                .map(Valence::value)
                .average()
                .toFloat()
        )

}
