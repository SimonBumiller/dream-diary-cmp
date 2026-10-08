package me.bumiller.ddcmp.domain.model.emotion

import kotlin.jvm.JvmInline

/**
 * Describes the emotional affect of an emotion.
 */
@JvmInline
value class Valence(val value: Float) {

    init {
        if(value !in -1F..1F)
            throw IllegalArgumentException("Emotional valence must be in [-1, 1}.")
    }

}