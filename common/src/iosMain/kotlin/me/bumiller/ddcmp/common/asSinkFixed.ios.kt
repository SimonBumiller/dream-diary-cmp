package me.bumiller.ddcmp.common

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.sink
import kotlinx.io.RawSink

public actual fun PlatformFile.asSinkFixed(): RawSink = sink()