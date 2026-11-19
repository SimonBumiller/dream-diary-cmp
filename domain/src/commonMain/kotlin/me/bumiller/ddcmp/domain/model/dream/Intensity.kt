package me.bumiller.ddcmp.domain.model.dream

/**
 * Classifies how intense a dream was.
 */
public enum class Intensity {

    /**
     * Dream had very low intensity.
     */
    VeryLow,

    /**
     * Dream had low intensity.
     */
    Low,

    /**
     * Dream had medium intensity.
     */
    Medium,

    /**
     * Dream had high intensity.
     */
    High,

    /**
     * Dream had very high intensity.
     */
    VeryHigh;

    public companion object {
        public fun fromOrdinal(ordinal: Int): Intensity = entries.find { it.ordinal == ordinal }!!
    }
}