package me.bumiller.ddcmp.domain.model.meta

/**
 * Models sorting modes.
 */
sealed interface SortMode {

    data object Created : SortMode
    data object Updated : SortMode

    /**
     * Sort modes for dreams.
     */
    sealed interface DreamSort : SortMode {
        data object Name : DreamSort
        data object Dreamt : DreamSort
        data object Confidentiality : DreamSort
        data object AverageValence : DreamSort
    }

    /**
     * Sort modes for emotions.
     */
    sealed interface EmotionSort : SortMode {
        data object Name : EmotionSort
    }

    /**
     * Sort modes for persons.
     */
    sealed interface PersonSort : SortMode {
        data object Name : PersonSort
    }
}