package me.bumiller.ddcmp.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

/**
 * Base class for view models in the app that contains utility methods and fields.
 */
open class AppViewModel: ViewModel() {

    /**
     * Shorthand for creating a stateflow that runs in the [viewModelScope].
     */
    protected fun <T> Flow<T>.stateIn(initial: T) =
        this.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initial)

}