package me.bumiller.ddcmp.ui.common

import io.github.vinceglb.filekit.PlatformFile

/**
 * Opens a platform-specified way to view the location of the file.
 */
expect fun viewDirectory(file: PlatformFile): Boolean