package me.bumiller.ddcmp.domain.model.meta

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Models an editable instance that can be edited on a specific point in time.
 */
@OptIn(ExperimentalTime::class)
interface Editable: Trackable {

    /**
     * The edit timestamp.
     */
    var edited: Instant

}