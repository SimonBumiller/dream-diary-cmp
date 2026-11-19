package me.bumiller.ddcmp.domain.model.dream

/**
 * Models different types of confidentiality that a dream can be.
 */
public enum class Confidentiality(

    /**
     * Whether the title can be previewed.
     */
    public val previewTitle: Boolean,

    /**
     * Whether the description can be previewed.
     */
    public val previewDescription: Boolean,

    /**
     * Whether the dream needs to be unlocked.
     */
    public val locked: Boolean

) {

    /**
     * No confidentiality.
     */
    None(true, true, false),

    /**
     * Medium confidentiality.
     */
    Medium(true, false, false),

    /**
     * High confidentiality.
     */
    Severe(false, false, true)

}