package me.bumiller.ddcmp.common

import io.sentry.Sentry

public actual fun captureException(exception: Exception) {
    Sentry.captureException(exception)
}