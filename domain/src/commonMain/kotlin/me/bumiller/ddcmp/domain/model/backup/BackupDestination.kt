package me.bumiller.ddcmp.domain.model.backup

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.isDirectory
import io.github.vinceglb.filekit.path
import me.bumiller.ddcmp.domain.model.meta.Trackable
import me.bumiller.ddcmp.domain.model.meta.Unique
import kotlin.time.Instant

/**
 * Destination for periodic backups.
 */
public sealed interface BackupDestination : Unique, Trackable {

    /**
     * Backup should be stored in a local directory.
     */
    public data class LocalDirectory(

        override val id: Long,
        override val created: Instant,

        val directory: PlatformFile

    ) : BackupDestination {

        override fun toString(): String = directory.path

    }

}

/**
 * Created a copy with varied id.
 */
public fun <T: BackupDestination> T.copy(id: Long = this.id): BackupDestination.LocalDirectory = when(this) {
    is BackupDestination.LocalDirectory -> BackupDestination.LocalDirectory(id, created, directory)
}