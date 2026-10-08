package me.bumiller.ddcmp.domain.model.emotion

/**
 * Groups emotions into four canonical types, based on their arousal and valence.
 */
enum class EmotionType {

    /**
     * High arousal, high valence.
     */
    Joy,

    /**
     * Low arousal, high valence.
     */
    Pleasure,

    /**
     * Low arousal, low valence.
     */
    Sadness,

    /**
     * High arousal, low valence.
     */
    Anger;

    companion object {

        /**
         * Gets the appropriate emotion type for an emotion.
         */
        fun from(e: EmotionCoordinate) =
            when (e.arousal.value) {
                in 0F..1F if e.valence.value in 0F..1F -> Joy
                in -1F..0F if e.valence.value in 0F..1F -> Pleasure
                in -1F..0F if e.valence.value in -1F..0F -> Sadness
                else -> Anger
            }

    }


}