package me.bumiller.ddcmp.core.io.common

import kotlin.use

/**
 * Wrapper around [use] so that suspend functions can be passed as lambda-parameters.
 */
suspend inline fun <T : AutoCloseable?, R> T.use(
    block: suspend (T) -> R
): R {
    return this.use { block(it) }
}