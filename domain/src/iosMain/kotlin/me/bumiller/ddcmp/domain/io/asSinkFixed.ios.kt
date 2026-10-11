package me.bumiller.ddcmp.domain.io

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.sink
import kotlinx.io.RawSink
import kotlin.math.sin

actual fun PlatformFile.asSinkFixed(): RawSink = sink()