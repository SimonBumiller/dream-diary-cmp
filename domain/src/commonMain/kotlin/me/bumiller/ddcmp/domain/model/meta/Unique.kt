package me.bumiller.ddcmp.domain.model.meta

/**
 * Interface for classes whose instances are semantically unique.
 */
interface Unique {

    /**
     * The unique identifier.
     */
    val id: Long

}