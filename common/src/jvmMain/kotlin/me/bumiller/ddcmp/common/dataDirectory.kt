package me.bumiller.ddcmp.common

import io.github.vinceglb.filekit.PlatformFile
import java.io.File

private const val AppName = "me.bumiller.ddcmp"

/**
 * Gets the root of the apps data-directory.
 */
fun dataDirectory(): PlatformFile {
    val osName = System.getProperty("os.name").lowercase()
    val userHome = System.getProperty("user.home")

    val dataDir = when {
        "win" in osName -> getWinDir(userHome)
        "linux" in osName -> getLinDir(userHome)
        "mac" in osName -> getMacDir(userHome)
        else -> getUnknownDir(userHome)
    }.let(::PlatformFile)

    return PlatformFile(dataDir, AppName)
}

private fun getUnknownDir(home: String): String = home

private fun getWinDir(home: String): String = System.getenv("APPDATA") ?: "$home/AppData/Roaming"

private fun getLinDir(home: String): String = System.getenv("XDG_DATA_HOME")
    ?.takeIf { it.isNotBlank() && File(it).isAbsolute }
    ?: "$home/.local/share"

private fun getMacDir(home: String): String = "$home/Library/Application Support"