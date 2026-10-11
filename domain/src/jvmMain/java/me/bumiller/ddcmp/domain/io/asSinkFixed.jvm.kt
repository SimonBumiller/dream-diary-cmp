package me.bumiller.ddcmp.domain.io

import io.github.vinceglb.filekit.sink

actual fun io.github.vinceglb.filekit.PlatformFile.asSinkFixed(): kotlinx.io.RawSink = sink()