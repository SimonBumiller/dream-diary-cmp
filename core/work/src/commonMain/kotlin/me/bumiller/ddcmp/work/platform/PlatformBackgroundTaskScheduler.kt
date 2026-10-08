package me.bumiller.ddcmp.work.platform

import kotlin.time.Duration

/**
 * Wrapper around platform-specific implementation of a scheduler.
 *
 * Technically, this isn't necessary, as the kmp-workmanager library we use is already multiplatform.
 * But that library does not cater to jvm-targets.
 * We add this intermediary interface so that we can implement a no-operation implementation for jvm.
 */
expect class PlatformBackgroundTaskScheduler {

    /**
     * Forwards configuration for a periodic work request.
     */
    suspend fun enqueuePeriodic(
        id: String,
        inputJson: String,
        interval: Duration,
        flex: Duration,
        initialDelay: Duration,
        workerClassName: String,
        tags: Set<String>
    ): Boolean

    /**
     * Forwards the cancelByTag request.
     */
    suspend fun cancelByTag(tag: String)

    /**
     * Gets the id's of the currently active tasks for a given tag.
     */
    suspend fun getForTag(tag: String): List<String>

}