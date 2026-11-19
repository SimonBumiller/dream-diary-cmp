package me.bumiller.ddcmp.domain.model.meta

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Models a trackable instance that was created on a specific point in time.
 */
public interface Trackable {

    /**
     * The creation timestamp.
     */
    public val created: Instant

}