package me.bumiller.ddcmp.domain.model.meta

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Models a trackable instance that was created on a specific point in time.
 */
@OptIn(ExperimentalTime::class)
interface Trackable {

    /**
     * The creation timestamp.
     */
    val created: Instant

}