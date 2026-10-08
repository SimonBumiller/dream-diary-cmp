package me.bumiller.ddcomp.core.database

import androidx.core.net.toUri
import io.github.vinceglb.filekit.AndroidFile
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.utils.toPath
import me.bumiller.ddcmp.domain.model.backup.BackupDestination
import me.bumiller.ddcomp.core.database.entity.BackupDestinationEntity

actual object PlatformMapping {

    actual fun backupDestination(model: BackupDestination) = model.run {
        val localDir = this as? BackupDestination.LocalDirectory

        val fileWrapper = localDir?.directory?.androidFile as? AndroidFile.FileWrapper
        val uriWrapper = localDir?.directory?.androidFile as? AndroidFile.UriWrapper

        BackupDestinationEntity(
            id = id,
            created = created,
            updated = created,
            localAndroidPath = fileWrapper?.file?.path,
            localAndroidUri = uriWrapper?.uri?.toString(),
            localIosUri = null,
            localJvmPath = null
        )
    }

    actual object Reverse {

        actual fun backupDestination(entity: BackupDestinationEntity): BackupDestination = entity.run {
            val isLocalPath = localAndroidPath != null
            val isLocalUri = localAndroidUri != null

            val isLocalDir = isLocalPath || isLocalUri

            if(isLocalDir) BackupDestination.LocalDirectory(
                id = id,
                created = created,
                directory = if(isLocalPath) PlatformFile(localAndroidPath.toPath())
                    else if(isLocalUri) PlatformFile(localAndroidUri.toUri())
                    else error("For local directory, either uri or path must be given.")
            )

            else error("No other backup destination candidate found.")
        }

    }

}