package me.bumiller.ddcmp.domain.io

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.div

/**
 * Platform specific fix for the [div] function.
 */
expect suspend fun PlatformFile.createChildFile(
    name: String,
    mimeType: String = "application/octet-stream"
): PlatformFile