package me.bumiller.ddcmp.core.io.common

import io.github.vinceglb.filekit.PlatformFile
import no.synth.kmpzip.io.SeekableSource

/**
 * Method to convert a PlatformFile to a [SeekableSource].
 * This is needed to allow random access for zip files.
 */
expect fun PlatformFile.asSeekableSource(): SeekableSource