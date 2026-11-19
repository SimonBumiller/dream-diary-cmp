package me.bumiller.ddcmp.common

/**
 * Capture an exception and report it to the error backend (i.e. sentry on android).
 */
public expect fun captureException(exception: Exception)