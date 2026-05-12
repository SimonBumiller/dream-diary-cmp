package me.bumiller.ddcmp.domain.model.meta

/**
 * A model that has searchable fields
 */
interface Searchable {

    /**
     * The fields searchable.
     */
    val searchableFields: List<String>

}