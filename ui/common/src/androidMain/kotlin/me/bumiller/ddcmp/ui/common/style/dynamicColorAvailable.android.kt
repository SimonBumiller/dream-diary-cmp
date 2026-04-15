package me.bumiller.ddcmp.ui.common.style

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(api=Build.VERSION_CODES.S)
actual fun dynamicColorAvailable(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S