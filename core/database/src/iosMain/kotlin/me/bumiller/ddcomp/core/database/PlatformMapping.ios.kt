package me.bumiller.ddcomp.core.database

import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.domain.model.backup.BackupDestination
import me.bumiller.ddcomp.core.database.entity.BackupDestinationEntity
import platform.Foundation.NSURL

actual object PlatformMapping {
    actual fun backupDestination(model: BackupDestination) = model.run {
        val localDir = model as? BackupDestination.LocalDirectory

        BackupDestinationEntity(
            id = id,
            created = created,
            updated = created,
            localAndroidPath = null,
            localAndroidUri = null,
            localIosUri = localDir?.directory?.nsUrl?.toString(),
            localJvmPath = null
        )
    }

    actual object Reverse {

        actual fun backupDestination(entity: BackupDestinationEntity): BackupDestination = entity.run {
            val isLocalUri = localIosUri != null

            if(isLocalUri) BackupDestination.LocalDirectory(
                id = id,
                created = created,
                directory = PlatformFile(NSURL(fileURLWithPath = localIosUri))
            )

            error("No other backup destination candidate found.")
        }

    }
}