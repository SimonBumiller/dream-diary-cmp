package me.bumiller.ddcomp.core.database

import io.github.vinceglb.filekit.PlatformFile
import me.bumiller.ddcmp.domain.model.backup.BackupDestination
import me.bumiller.ddcomp.core.database.entity.BackupDestinationEntity

actual object PlatformMapping {
    actual fun backupDestination(model: BackupDestination) = model.run {
        val localDir = model as? BackupDestination.LocalDirectory

        BackupDestinationEntity(
            id = id,
            created = created,
            updated = created,
            localAndroidUri = null,
            localAndroidPath = null,
            localIosUri = null,
            localJvmPath = localDir?.directory?.file?.path
        )
    }

    actual object Reverse {

        actual fun backupDestination(entity: BackupDestinationEntity): BackupDestination = entity.run {
            val isLocalPath = localJvmPath != null

            if(isLocalPath) BackupDestination.LocalDirectory(
                id = id,
                created = created,
                directory = PlatformFile(localJvmPath)
            )

            error("No other backup destination candidate found.")
        }

    }
}