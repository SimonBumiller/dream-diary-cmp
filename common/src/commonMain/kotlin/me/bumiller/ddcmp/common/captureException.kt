package me.bumiller.ddcmp.common

/**
 * Capture an exception and report it to the error backend (i.e. sentry on android).
 */
expect fun captureException(exception: Exception)