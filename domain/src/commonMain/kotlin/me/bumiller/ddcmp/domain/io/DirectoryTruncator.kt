package me.bumiller.ddcmp.domain.io

import io.github.vinceglb.filekit.PlatformFile

/**
 * Truncates directories.
 */
interface DirectoryTruncator {

    /**
     * Deletes so many files that at a maximum, [maxCount] files are present in [directory].
     * Starts with old files.
     *
     * @return The number of deleted files.
     */
    suspend fun ensureMaxFileCount(directory: PlatformFile, maxCount: Int): Int

}