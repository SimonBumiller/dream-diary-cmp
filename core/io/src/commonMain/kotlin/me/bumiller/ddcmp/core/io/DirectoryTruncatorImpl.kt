package me.bumiller.ddcmp.core.io

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.bookmarkData
import io.github.vinceglb.filekit.createdAt
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.list
import me.bumiller.ddcmp.domain.io.DirectoryTruncator
import kotlin.time.Instant

internal class DirectoryTruncatorImpl: DirectoryTruncator {

    override suspend fun ensureMaxFileCount(
        directory: PlatformFile,
        maxCount: Int
    ): Int {
        require(maxCount >= 0)

        val children = directory.list()

        if(children.size <= maxCount) return 0

        val deleteCount = children.size - maxCount

        val toDeleteChildren = children
            .sortedBy { child ->
                child.createdAt() ?: Instant.DISTANT_PAST
            }
            .take(deleteCount)

        toDeleteChildren.forEach { child ->
            child.delete()
        }

        return deleteCount
    }

}