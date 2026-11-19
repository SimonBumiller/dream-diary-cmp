package me.bumiller.ddcmp.ui.common

import io.github.vinceglb.filekit.PlatformFile

/**
 * Opens a platform-specified way to view the file content.
 */
public expect fun viewFile(file: PlatformFile, mimeType: String): Boolean