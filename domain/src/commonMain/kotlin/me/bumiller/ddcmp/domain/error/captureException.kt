package me.bumiller.ddcmp.domain.error

/**
 * Capture an exception and report it to the error backend (i.e. sentry on android).
 */
expect fun captureException(exception: Exception)