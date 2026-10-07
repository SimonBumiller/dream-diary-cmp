package me.bumiller.ddcmp.core.io.common

import kotlinx.io.Buffer
import kotlinx.io.RawSink

internal class CountingSink(val delegate: RawSink): RawSink by delegate {

    private var writtenByteCount = 0L

    fun writtenByteCount() = writtenByteCount

    override fun write(source: Buffer, byteCount: Long) {
        writtenByteCount += byteCount
        delegate.write(source, byteCount)
    }
}

/**
 * Adds the capability of counting written bytes to a RawSink.
 */
internal fun RawSink.countingSink(): CountingSink = CountingSink(this)