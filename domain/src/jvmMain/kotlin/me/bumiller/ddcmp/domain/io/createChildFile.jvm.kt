package me.bumiller.ddcmp.domain.io

import io.github.vinceglb.filekit.PlatformFile
import java.io.File

actual suspend fun PlatformFile.createChildFile(
    name: String,
    mimeType: String
): PlatformFile {
    val child = File(file, name)

    child.createNewFile()

    return PlatformFile(child)
}