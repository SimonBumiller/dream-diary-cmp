package me.bumiller.ddcmp.domain.io

import io.github.vinceglb.filekit.PlatformFile

/**
 * Truncates directories.
 */
public interface DirectoryTruncator {

    /**
     * Deletes so many files that at a maximum, [maxCount] files are present in [directory].
     * Starts with old files.
     *
     * @return The number of deleted files.
     */
    public suspend fun ensureMaxFileCount(directory: PlatformFile, maxCount: Int): Int

}