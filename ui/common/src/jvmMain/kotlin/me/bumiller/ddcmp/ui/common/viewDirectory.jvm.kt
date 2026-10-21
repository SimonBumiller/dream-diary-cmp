package me.bumiller.ddcmp.ui.common

import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.common.captureException
import java.awt.Desktop

actual fun viewDirectory(file: PlatformFile): Boolean {
    val desktop = Desktop.getDesktop()

    return try {
        if (desktop.isSupported(Desktop.Action.BROWSE_FILE_DIR)) {
            desktop.browseFileDirectory(file.file)
        } else {
            desktop.open(file.file.parentFile)
        }

        true
    } catch (e: Exception) {
        e.printStackTrace()
        captureException(e)
        false
    }
}