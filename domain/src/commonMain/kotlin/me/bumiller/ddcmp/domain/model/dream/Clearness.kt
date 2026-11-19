package me.bumiller.ddcmp.domain.model.dream

/**
 * Classifies how clear the memory of a dream was.
 */
public enum class Clearness {

    /**
     * Dream had very low clearness.
     */
    VeryLow,
    /**
     * Dream had low clearness.
     */
    Low,

    /**
     * Dream had medium clearness.
     */
    Medium,

    /**
     * Dream had high clearness.
     */
    High,

    /**
     * Dream had very high clearness.
     */
    VeryHigh;

    public companion object {
        public fun fromOrdinal(ordinal: Int): Clearness = entries.find { it.ordinal == ordinal }!!
    }
}