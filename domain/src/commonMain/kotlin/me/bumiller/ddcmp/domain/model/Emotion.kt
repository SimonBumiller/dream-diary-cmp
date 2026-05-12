package me.bumiller.ddcmp.domain.model

import me.bumiller.ddcmp.domain.model.meta.Editable
import me.bumiller.ddcmp.domain.model.meta.Searchable
import me.bumiller.ddcmp.domain.model.meta.Unique
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Describes an emotion.
 */
@OptIn(ExperimentalTime::class)
data class Emotion(

    override val id: Long,
    override val created: Instant,
    override var edited: Instant,

    /**
     * The arousal level.
     */
    override var arousal: Arousal,

    /**
     * The valence level.
     */
    override var valence: Valence,

    /**
     * The name of the emotion.
     */
    var name: String

) : Unique, Editable, Searchable, EmotionCoordinate {

    override val searchableFields: List<String>
        get() = listOf(name)

}