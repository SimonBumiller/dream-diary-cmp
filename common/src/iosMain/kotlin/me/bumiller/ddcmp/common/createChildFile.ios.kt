package me.bumiller.ddcmp.common

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.div
import io.github.vinceglb.filekit.path
import platform.Foundation.NSFileManager

actual suspend fun PlatformFile.createChildFile(
    name: String,
    mimeType: String
): PlatformFile {
    val child = this / name

    NSFileManager.defaultManager.createFileAtPath(
        path = child.path,
        contents = null,
        attributes = null
    )

    return child
}