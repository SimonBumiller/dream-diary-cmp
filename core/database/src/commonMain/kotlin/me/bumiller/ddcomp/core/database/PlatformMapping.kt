package me.bumiller.ddcomp.core.database

import me.bumiller.ddcmp.domain.model.backup.BackupDestination
import me.bumiller.ddcomp.core.database.entity.BackupDestinationEntity

/**
 * Platform dependant model-mapping.
 */
expect object PlatformMapping {

    fun backupDestination(model: BackupDestination): BackupDestinationEntity

    object Reverse {

        fun backupDestination(entity: BackupDestinationEntity): BackupDestination

    }

}