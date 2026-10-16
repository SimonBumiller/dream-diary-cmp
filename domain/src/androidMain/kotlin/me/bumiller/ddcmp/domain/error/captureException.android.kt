package me.bumiller.ddcmp.domain.error

import io.sentry.Sentry

actual fun captureException(exception: Exception) {
    Sentry.captureException(exception)
}