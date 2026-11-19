package me.bumiller.ddcmp.domain.model.emotion

import kotlin.jvm.JvmInline

/**
 * Describes the emotional strength of an emotion.
 */
@JvmInline
public value class Arousal(public val value: Float) {

    init {
        if(value !in -1F..1F)
            throw IllegalArgumentException("Emotional arousal must be in [-1, 1}.")
    }

}