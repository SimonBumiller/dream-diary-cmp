package me.bumiller.ddcmp.domain.model.meta

/**
 * Models sorting modes.
 */
public sealed interface SortMode {

    public data object Created : SortMode
    public data object Updated : SortMode

    /**
     * Sort modes for dreams.
     */
    public sealed interface DreamSort : SortMode {
        public data object Name : DreamSort
        public data object Dreamt : DreamSort
        public data object Confidentiality : DreamSort
        public data object AverageValence : DreamSort
        public data object Length: DreamSort
        public data object Clearness: DreamSort
        public data object Intensity: DreamSort
    }

    /**
     * Sort modes for tags.
     */
    public sealed interface TagSort : SortMode {
       public data object Color: TagSort
        public data object Name: TagSort
    }

    /**
     * Sort modes for emotions.
     */
    public sealed interface EmotionSort : SortMode {
        public data object Name : EmotionSort
    }

    /**
     * Sort modes for persons.
     */
    public sealed interface PersonSort : SortMode {
        public data object Name : PersonSort
    }
}