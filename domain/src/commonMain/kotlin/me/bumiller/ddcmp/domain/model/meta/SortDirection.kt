package me.bumiller.ddcmp.domain.model.meta

/**
 * Models sort directions.
 */
public enum class SortDirection {

    /**
     * Lower values first.
     */
    Ascending,

    /**
     * Higher values first.
     */
    Descending;

    public operator fun not(): SortDirection = when (this) {
        Ascending -> Descending
        Descending -> Ascending
    }

}