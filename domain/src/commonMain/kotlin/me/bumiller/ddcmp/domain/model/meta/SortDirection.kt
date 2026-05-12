package me.bumiller.ddcmp.domain.model.meta

/**
 * Models sort directions.
 */
enum class SortDirection {

    /**
     * Lower values first.
     */
    Ascending,

    /**
     * Higher values first.
     */
    Descending;

    operator fun not() = when (this) {
        Ascending -> Descending
        Descending -> Ascending
    }

}