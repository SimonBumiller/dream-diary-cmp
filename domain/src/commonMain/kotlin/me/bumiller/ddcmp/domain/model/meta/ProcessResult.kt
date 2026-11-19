package me.bumiller.ddcmp.domain.model.meta

import kotlin.time.Duration
import kotlin.time.Instant

/**
 * An entity that has a start and end timestamp.
 */
public interface ProcessResult {

    /**
     * When the process was started.
     */
    public val started: Instant

    /**
     * When the process was ended.
     */
    public val ended: Instant

    /**
     * The total running time of the process.
     */
    public val processLength: Duration
        get() = (ended - started).absoluteValue

}