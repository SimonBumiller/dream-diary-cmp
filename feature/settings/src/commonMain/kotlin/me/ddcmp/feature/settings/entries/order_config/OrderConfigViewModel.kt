package me.ddcmp.feature.settings.entries.order_config

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.asStateFlow
import me.bumiller.ddcmp.domain.model.backup.BackupDestination
import me.bumiller.ddcmp.domain.model.io.ExportOption
import me.bumiller.ddcmp.domain.model.io.IOFileType
import me.bumiller.ddcmp.domain.usecase.backup.SaveBackupOrderUseCase
import me.bumiller.ddcmp.ui.common.AppViewModel
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.bumiller.ddcmp.ui.common.model.IOOptionType
import me.ddcmp.feature.settings.entries.order_config.OrderConfigViewModel.UiEvent
import me.ddcmp.feature.settings.entries.order_config.OrderConfigViewModel.ViewModelEvent
import me.ddcmp.feature.settings.entries.order_config.OrderConfigViewModel.ViewModelEvent.OrderSaved
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

internal class OrderConfigViewModel(
    private val saveOrder: SaveBackupOrderUseCase
) : AppViewModel<UiEvent, ViewModelEvent>() {

    init {
        registerUiState(OrderState())
        registerUiState(UiState())
    }

    /**
     * The ui state containing information about the order.
     */
    val orderState = uiState<OrderState>()
        .asStateFlow()

    /**
     * The ui state containing information about the user interface.
     */
    val uiState = uiState<UiState>()
        .asStateFlow()

    override suspend fun handleEvent(event: UiEvent) = when (event) {
        is UiEvent.ChangeBackupDirectory -> updateUiState<OrderState> { it.copy(backupDirectory = event.destination) }
        is UiEvent.ChangeFirstBackupImmediately -> updateUiState<OrderState> {
            it.copy(
                firstBackupImmediately = event.firstBackupImmediately
            )
        }

        is UiEvent.ChangeIOFileType -> updateUiState<OrderState> { it.copy(ioFileType = event.fileType) }
        is UiEvent.ChangeIOOption -> updateUiState<OrderState> { it.copy(ioOption = event.type) }
        is UiEvent.ChangeKeepLast -> updateUiState<OrderState> { it.copy(keepLast = event.keepLast) }
        is UiEvent.ChangeScheduleDays -> updateUiState<OrderState> { it.copy(scheduleDays = event.scheduleDays) }
        is UiEvent.ChangeScheduleHours -> updateUiState<OrderState> { it.copy(scheduleHours = event.scheduleHours) }
        is UiEvent.ChangeLimitlessRetention -> updateUiState<OrderState> {
            it.copy(
                limitlessRetention = event.limitless
            )
        }

        UiEvent.ToggleFiletypeDropdownVisibility -> updateUiState<UiState> {
            it.copy(
                showFiletypeDropdown = !it.showFiletypeDropdown
            )
        }

        UiEvent.ClickCancel -> TODO()
        UiEvent.ClickSaveOrder -> saveConfig()
    }

    private suspend fun saveConfig() {
        if (!orderState.value.isValid()) return

        val order = orderState.value.asUsecaseInput()
            .let { saveOrder(it) }

        fireEvent(OrderSaved(order.id))
    }

    data class UiState(

        /**
         * Whether the io file type dropdown is visible
         */
        val showFiletypeDropdown: Boolean = false

    )

    /**
     * The state containing needed data to construct the order config.
     */
    data class OrderState(

        /**
         * The destination into which the backup will be saved.
         */
        val backupDirectory: PlatformFile? = null,

        /**
         * The days part of the scheduling period.
         */
        val scheduleDays: Int? = null,

        /**
         * The hours part of the scheduling period.
         */
        val scheduleHours: Int? = null,

        /**
         * Whether the first backup should be performed immediately.
         */
        val firstBackupImmediately: Boolean = true,

        /**
         * Which export type should be used.
         */
        val ioOption: IOOptionType = IOOptionType.Normal,

        /**
         * For normal exports, which data representation should be used.
         */
        val ioFileType: IOFileType = IOFileType.Json,

        /**
         * Whether keepLast = null in the resulting order
         */
        val limitlessRetention: Boolean = true,

        /**
         * The input to the keepLast
         */
        val keepLast: String = 5.toString()

    ) {

        /**
         * Whether the storage configuration is valid.
         */
        fun isStorageValid() = backupDirectory != null

        /**
         * Whether the scheduling information is valid.
         */
        fun isScheduleValid() = scheduleDays != null && scheduleHours != null && // Schedule set
                scheduleDays in 0..30 && scheduleHours in 0..24 && // Schedule domain valid
                scheduleDays + scheduleDays != 0 // At least one is non-zero

        /**
         * Whether the options are valid.
         */
        fun isOptionsValid() = limitlessRetention || keepLast.toIntOrNull() != null

        /**
         * Whether the whole order is valid.
         */
        fun isValid() = isStorageValid() && isScheduleValid() && isOptionsValid()

        fun asUsecaseInput() = SaveBackupOrderUseCase.I(
            destination = BackupDestination.LocalDirectory(
                0,
                Clock.System.now(),
                backupDirectory!!
            ),
            period = scheduleDays!!.days + scheduleHours!!.hours,
            initialTime = Clock.System.now().let {
                if (firstBackupImmediately) it
                else it + 30.minutes
            },
            keepLast = if (limitlessRetention) null else keepLast.toInt(),
            exportOption = when (ioOption) {
                IOOptionType.Legacy -> ExportOption.Legacy
                IOOptionType.Normal -> ExportOption.Normal(ioFileType)
            }
        )

    }

    sealed interface UiEvent : UIE {
        data class ChangeBackupDirectory(val destination: PlatformFile?) : UiEvent
        data class ChangeScheduleDays(val scheduleDays: Int?) : UiEvent
        data class ChangeScheduleHours(val scheduleHours: Int?) : UiEvent
        data class ChangeFirstBackupImmediately(val firstBackupImmediately: Boolean) : UiEvent
        data class ChangeIOOption(val type: IOOptionType) : UiEvent
        data class ChangeIOFileType(val fileType: IOFileType) : UiEvent
        data class ChangeKeepLast(val keepLast: String) : UiEvent
        data class ChangeLimitlessRetention(val limitless: Boolean) : UiEvent

        data object ToggleFiletypeDropdownVisibility : UiEvent

        data object ClickSaveOrder : UiEvent
        data object ClickCancel : UiEvent
    }

    sealed interface ViewModelEvent : VME {
        data class OrderSaved(val orderId: Long) : ViewModelEvent
        data object Cancelled : ViewModelEvent
    }

}