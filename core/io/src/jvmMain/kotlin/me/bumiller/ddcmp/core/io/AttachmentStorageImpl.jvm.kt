package me.bumiller.ddcmp.core.io

import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.common.dataDirectory

private const val AttachmentsDirectoryName = "attachments"

internal actual fun attachmentDirectory(): PlatformFile {
    val dataDirectory = dataDirectory()
    return PlatformFile(dataDirectory, AttachmentsDirectoryName)
}