package me.bumiller.ddcmp.domain.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Application-Scoped coroutine scope.
 */
class AppCoroutineScope : CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.Default)