package me.bumiller.ddcmp.ui.common

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.io.IOException
import me.bumiller.ddcmp.common.captureException
import java.awt.Desktop

actual fun viewFile(
    file: PlatformFile,
    mimeType: String
): Boolean {
    return try {
        Desktop.getDesktop().open(file.file)
        true
    } catch(e: IOException) {
        captureException(e)
        false
    }
}