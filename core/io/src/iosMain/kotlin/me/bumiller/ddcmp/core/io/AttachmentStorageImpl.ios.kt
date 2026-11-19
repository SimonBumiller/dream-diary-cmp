package me.bumiller.ddcmp.core.io

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
internal actual fun attachmentDirectory(): PlatformFile {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )

    return documentDirectory?.absoluteURL
        ?.let { PlatformFile(it) }
        ?.let { PlatformFile(it, "attachments") }
        ?: throw IllegalStateException("Could not create attachments directory")
}