package me.ddcmp.feature.settings.entries.order_detail.model

import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder

internal fun PeriodicBackupOrder.state() =
    when(active) {
        true -> OrderState.Running
        false -> when(wasActive) {
            true -> OrderState.Canceled
            false -> OrderState.Saved
        }
    }

internal enum class OrderState {
    Saved, Running, Canceled
}