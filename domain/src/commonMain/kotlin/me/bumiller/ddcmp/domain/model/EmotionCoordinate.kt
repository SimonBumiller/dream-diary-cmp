package me.bumiller.ddcmp.domain.model

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

}