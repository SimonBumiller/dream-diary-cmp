package me.bumiller.ddcmp.work

import kotlinx.serialization.json.Json
import me.bumiller.ddcmp.domain.infrastructure.BackupScheduler
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder
import me.bumiller.ddcmp.work.platform.PlatformBackgroundTaskScheduler
import me.bumiller.ddcmp.work.worker.PeriodicBackupWorkInput
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

private const val TAG = "me.bumiller.ddcmp.work.periodic_backup"

private val Flex = 10.minutes

private val PeriodicBackupOrder.taskId: String
    get() = "$TAG-$id"

internal class PlatformBackupScheduler(
    private val scheduler: PlatformBackgroundTaskScheduler
) : BackupScheduler {

    override suspend fun scheduleOrder(order: PeriodicBackupOrder): Boolean {
        val now = Clock.System.now()

        val timeDifference = order.initialTime - now
        val initialDelay = if(timeDifference.isNegative()) Duration.ZERO
            else timeDifference

        val input = PeriodicBackupWorkInput(order.id)

        return scheduler.enqueuePeriodic(
            id = order.taskId,
            interval = order.period,
            flex = Flex,
            initialDelay = initialDelay,
            workerClassName = "PeriodicBackupWorker",
            tags = setOf(TAG),
            inputJson = Json.encodeToString(input)
        )
    }

    override suspend fun cancelAll(): Int {
        val count = scheduler.getForTag(TAG).size
        scheduler.cancelByTag(TAG)
        return count
    }

}