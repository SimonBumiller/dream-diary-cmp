package me.bumiller.ddcmp.domain.model.backup

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.isDirectory
import me.bumiller.ddcmp.domain.model.meta.Trackable
import me.bumiller.ddcmp.domain.model.meta.Unique
import kotlin.time.Instant

/**
 * Destination for periodic backups.
 */
sealed interface BackupDestination : Unique, Trackable {

    /**
     * Backup should be stored in a local directory.
     */
    data class LocalDirectory(

        override val id: Long,
        override val created: Instant,

        val directory: PlatformFile

    ) : BackupDestination {

        init {
            require(directory.isDirectory()) { "LocalDirectory can only wrap a directory file!" }
        }

    }

}

/**
 * Created a copy with varied id.
 */
fun <T: BackupDestination> T.copy(id: Long = this.id) = when(this) {
    is BackupDestination.LocalDirectory -> BackupDestination.LocalDirectory(id, created, directory)
}