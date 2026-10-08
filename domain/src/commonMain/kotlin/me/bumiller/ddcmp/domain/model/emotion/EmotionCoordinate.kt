package me.bumiller.ddcmp.domain.model.emotion

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Base class for something that carries information about emotion-placement.
 */
interface EmotionCoordinate {

    /**
     * The valence, i.e. X-axis placement.
     */
    val valence: Valence

    /**
     * The arousal, i.e. Y-axis placement.
     */
    val arousal: Arousal

    /**
     * Calculates the euclidean distance to another coordinate.
     */
    fun distanceTo(other: EmotionCoordinate): Float = sqrt(
        abs(other.valence.value - valence.value).pow(2) + abs(other.arousal.value - arousal.value).pow(
            2
        )
    )

}