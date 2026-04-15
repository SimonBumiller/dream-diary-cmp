package me.bumiller.ddcmp

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.bumiller.ddcmp.domain.model.Settings
import me.bumiller.ddcmp.domain.usecase.settings.GetSettingsUseCase
import me.bumiller.ddcmp.navigation.MainRoutes
import me.bumiller.ddcmp.ui.common.UIE
import me.bumiller.ddcmp.ui.common.VME
import me.bumiller.ddcmp.ui.common.AppViewModel as BaseViewModel

/**
 * Viewmodel that controls the top-level app functions.
 */
internal class AppViewmodel(

    /**
     * The usecase to get the settings.
     */
    getSettings: GetSettingsUseCase

) : BaseViewModel<AppViewmodel.UiEvent, AppViewmodel.ViewModelEvent>() {

    internal interface UiEvent: UIE
    internal interface ViewModelEvent: VME

    private val navBackStack = MutableStateFlow<List<MainRoutes>>(emptyList())

    init {
        viewModelScope.launch {
            val settings = getSettings(Unit).first()
            val initialEntry = if (settings.onboardingFinished) MainRoutes.Home
            else MainRoutes.Onboarding

            navBackStack.value = listOf(initialEntry)
        }
    }

    /**
     * The current state of the app.
     */
    val state = getSettings(Unit)
        .map { AppState(it) }
        .stateIn(null)

    /**
     * The ui state of the app.
     */
    val uiState = navBackStack
        .map { AppUiState(it) }
        .stateIn(AppUiState(emptyList()))

    /**
     * Navigates to a specific route.
     */
    fun navigateTo(route: MainRoutes, replace: Boolean = false) {
        val old = navBackStack.value
        val new = if(replace) old - old.last() + route else old + route
        navBackStack.value = new
    }

    /**
     * Navigates back one route.
     */
    fun navigateBack() {
        navBackStack.value = navBackStack.value.dropLast(1)
    }

    override suspend fun handleEvent(event: UiEvent) {

    }

}

/**
 * The ui state of the app.
 */
internal data class AppUiState(

    /**
     * The navigation back stack.
     */
    val navBackStack: List<MainRoutes>

)

/**
 * The state of the app.
 */
internal data class AppState(

    /**
     * The current settings.
     */
    val settings: Settings

)