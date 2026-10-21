package me.bumiller.ddcmp.core.io

import io.github.vinceglb.filekit.PlatformFile
import org.koin.core.context.GlobalContext

import android.content.Context
import java.io.File

private const val AttachmentsDirectoryName = "attachments"

internal actual fun attachmentDirectory(): PlatformFile {
    val context = GlobalContext.get().get<Context>()

    val filesDir: File = context.filesDir
    val attachmentsDir = File(filesDir, AttachmentsDirectoryName)
    return PlatformFile(attachmentsDir)
}