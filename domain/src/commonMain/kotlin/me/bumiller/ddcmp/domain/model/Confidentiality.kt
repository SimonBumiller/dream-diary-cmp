package me.bumiller.ddcmp.domain.model

/**
 * Models different types of confidentiality that a dream can be.
 */
enum class Confidentiality(

    /**
     * Whether the title can be previewed.
     */
    val previewTitle: Boolean,

    /**
     * Whether the description can be previewed.
     */
    val previewDescription: Boolean,

    /**
     * Whether the dream needs to be unlocked.
     */
    val locked: Boolean

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