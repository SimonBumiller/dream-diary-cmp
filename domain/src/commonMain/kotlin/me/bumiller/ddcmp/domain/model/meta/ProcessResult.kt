package me.bumiller.ddcmp.domain.model.meta

import kotlin.time.Duration
import kotlin.time.Instant

/**
 * An entity that has a start and end timestamp.
 */
interface ProcessResult {

    /**
     * When the process was started.
     */
    val started: Instant

    /**
     * When the process was ended.
     */
    val ended: Instant

    /**
     * The total running time of the process.
     */
    val processLength: Duration
        get() = (ended - started).absoluteValue

}