package me.bumiller.ddcmp.common

import io.sentry.Sentry

actual fun captureException(exception: Exception) {
    Sentry.captureException(exception)
}