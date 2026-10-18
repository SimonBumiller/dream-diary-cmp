package me.bumiller.ddcmp.work.worker

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import me.bumiller.ddcmp.domain.usecase.backup.PerformBackupOrderUseCase

@Serializable
internal data class PeriodicBackupWorkInput(

    val backupOrderId: Long

)

/**
 * Performs the work for the periodic backup.
 *
 * Uses several domain use-cases.
 *
 * @param inputString The input, adhering to [PeriodicBackupWorkInput]
 * @return The error message, or null for success
 */
internal suspend fun periodicBackupWork(
    inputString: String,
    performBackup: PerformBackupOrderUseCase
): String? {
    val orderId = Json.decodeFromString<PeriodicBackupWorkInput>(inputString).backupOrderId

    val success = performBackup(PerformBackupOrderUseCase.I(backupOrderId = orderId))

    return when (success) {
        true -> "A problem occurred trying to write the backup file"
        false -> null
    }
}