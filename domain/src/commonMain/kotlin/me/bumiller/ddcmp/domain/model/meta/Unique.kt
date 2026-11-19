package me.bumiller.ddcmp.domain.model.meta

/**
 * Interface for classes whose instances are semantically unique.
 */
public interface Unique {

    /**
     * The unique identifier.
     */
    public val id: Long

}