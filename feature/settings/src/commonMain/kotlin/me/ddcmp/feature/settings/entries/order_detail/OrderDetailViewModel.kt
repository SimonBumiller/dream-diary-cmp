package me.ddcmp.feature.settings.entries.order_detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.onEach
import me.bumiller.ddcmp.domain.model.io.ExportResult
import me.bumiller.ddcmp.domain.usecase.backup.CancelBackupOrdersUseCase
import me.bumiller.ddcmp.domain.usecase.backup.GetBackupOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.GetBackupsForOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.ScheduleBackupOrderUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.ddcmp.feature.settings.entries.order_detail.OrderDetailViewModel.UiEvent
import me.ddcmp.feature.settings.entries.order_detail.OrderDetailViewModel.ViewModelEvent
import me.ddcmp.feature.settings.entries.order_detail.OrderDetailViewModel.ViewModelEvent.NavigateBack
import me.ddcmp.feature.settings.entries.order_detail.model.OrderState
import me.ddcmp.feature.settings.entries.order_detail.model.state
import me.ddcmp.feature.settings.navigation.SettingsRoutes

internal class OrderDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getOrder: GetBackupOrderUseCase,
    getBackups: GetBackupsForOrderUseCase,
    private val schedule: ScheduleBackupOrderUseCase,
    private val cancel: CancelBackupOrdersUseCase
) : AppViewModel<UiEvent, ViewModelEvent>() {

    init {
        registerUiState(UiState())
    }

    private val orderId = savedStateHandle.toRoute<SettingsRoutes.OrderDetail>().id

    /**
     * The ui state-
     */
    val uiState = uiState<UiState>()
        .stateIn(null)

    /**
     * The order that is displayed, null initially.
     */
    val order = getOrder(GetBackupOrderUseCase.I(id = orderId, active = false))
        .stateIn(null)

    /**
     * The orders for the displayed order, null initially.
     */
    val orderBackups = getBackups(orderId)
        .onEach {
            if(it.mostRecentBackup?.export !is ExportResult.Failure)
                updateUiState<UiState> { it.copy(recentBackupErrorDialogVisible = false) }
        }
        .stateIn(null)

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        UiEvent.ClickBack -> fireEvent(NavigateBack)
        UiEvent.TriggerNegativeAction -> when (order.value?.state()) {
            OrderState.Saved -> deleteOrder()
            OrderState.Running -> stopOrder()
            else -> {}
        }

        UiEvent.TriggerPositiveAction -> when (order.value?.state()) {
            OrderState.Saved -> scheduleOrder()
            else -> {}
        }

        UiEvent.ToggleRecentBackupErrorDialog -> updateUiState<UiState> {
            it.copy(recentBackupErrorDialogVisible = !it.recentBackupErrorDialogVisible)
        }
    }

    private suspend fun scheduleOrder() {
        schedule(orderId)
    }

    private suspend fun deleteOrder() {
        // No deletion is actually performed.
        // We just navigate away from the detail screen.
        // Because the order is not active, it won't be shown again.
        // After closing the detail screen, user is navigated back to the setup wizard.
        fireEvent(ViewModelEvent.NavigateToFreshSetup)
    }

    private suspend fun stopOrder() {
        cancel(Unit)
    }

    /**
     * The ui state.
     */
    data class UiState(

        /**
         * Whether the dialog for displaying information about the recent backups export failure should be expanded.
         */
        val recentBackupErrorDialogVisible: Boolean = false

    )

    sealed interface UiEvent : UIE {
        data object TriggerPositiveAction : UiEvent
        data object TriggerNegativeAction : UiEvent
        data object ClickBack : UiEvent
        data object ToggleRecentBackupErrorDialog: UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data object NavigateBack : ViewModelEvent
        data object NavigateToFreshSetup: ViewModelEvent
    }

}