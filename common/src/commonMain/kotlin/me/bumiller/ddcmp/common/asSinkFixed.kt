package me.bumiller.ddcmp.common

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.sink
import kotlinx.io.RawSink

/**
 * Platform specific fix for the [sink] function.
 */
expect fun PlatformFile.asSinkFixed(): RawSink