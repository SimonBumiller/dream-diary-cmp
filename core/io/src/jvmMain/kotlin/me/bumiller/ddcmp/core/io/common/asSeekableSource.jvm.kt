package me.bumiller.ddcmp.core.io.common

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.path
import no.synth.kmpzip.io.SeekableSource
import no.synth.kmpzip.io.fileSeekableSource

actual fun PlatformFile.asSeekableSource(): SeekableSource {
    return fileSeekableSource(this.path)
}