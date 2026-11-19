package me.bumiller.ddcmp.domain.model.meta

/**
 * A model that has searchable fields
 */
public interface Searchable {

    /**
     * The fields searchable.
     */
    public val searchableFields: List<String>

}