package me.bumiller.ddcmp.work.platform

import dev.brewkits.kmpworkmanager.background.domain.BackgroundTaskScheduler
import dev.brewkits.kmpworkmanager.background.domain.Constraints
import dev.brewkits.kmpworkmanager.background.domain.ExistingPolicy
import dev.brewkits.kmpworkmanager.background.domain.QueriedTask
import dev.brewkits.kmpworkmanager.background.domain.ScheduleResult
import dev.brewkits.kmpworkmanager.background.domain.TaskTrigger
import kotlin.time.Duration

actual class PlatformBackgroundTaskScheduler(
    private val delegate: BackgroundTaskScheduler
) {
    actual suspend fun enqueuePeriodic(
        id: String,
        inputJson: String,
        interval: Duration,
        initialDelay: Duration,
        workerClassName: String,
        tags: Set<String>
    ): Boolean = delegate.enqueue(
        id = id,
        trigger = TaskTrigger.Periodic(
            intervalMs = interval.inWholeMilliseconds,
            initialDelayMs = initialDelay.inWholeMilliseconds
        ),
        workerClassName = workerClassName,
        constraints = Constraints(),
        inputJson = inputJson,
        policy = ExistingPolicy.KEEP,
        tags = tags,
        deadlineMs = null
    ) == ScheduleResult.ACCEPTED

    actual suspend fun cancelByTag(tag: String) = delegate.cancelByTag(tag)

    actual suspend fun getForTag(tag: String): List<String> =
        delegate.queryTasks(tags = setOf(tag))
            .map(QueriedTask::id)
}