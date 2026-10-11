package me.bumiller.ddcmp.work.platform

import kotlin.time.Duration

actual class PlatformBackgroundTaskScheduler {
    actual suspend fun enqueuePeriodic(
        id: String,
        inputJson: String,
        interval: Duration,
        initialDelay: Duration,
        workerClassName: String,
        tags: Set<String>
    ): Boolean = false

    actual suspend fun cancelByTag(tag: String) {
    }

    actual suspend fun getForTag(tag: String): List<String> = emptyList()
}