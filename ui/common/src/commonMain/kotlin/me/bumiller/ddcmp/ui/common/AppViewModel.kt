package me.bumiller.ddcmp.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.reflect.KClass

/**
 * Marker interface for any event fired by the ui.
 */
public interface UIE

/**
 * Marker interface for any event fired by the view model.
 */
public interface VME

/**
 * Base class for view models in the app that contains utility methods and fields.
 */
public abstract class AppViewModel<UiEvent : UIE, ViewModelEvent : VME> : ViewModel() {

    /**
     * Shorthand for creating a stateflow that runs in the [viewModelScope].
     */
    protected fun <T> Flow<T>.stateIn(initial: T): StateFlow<T> =
        this.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initial)

    //
    // Events
    //

    /**
     * Handles the event passed by the ui.
     */
    protected abstract suspend fun handleEvent(event: UiEvent)

    /**
     * Will queue the event to be handled by the view model.
     *
     * @param uiEvent The event from the ui.
     */
    public fun onEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            handleEvent(uiEvent)
        }
    }

    private val _events = MutableSharedFlow<ViewModelEvent>()

    /**
     * The events fired by the view model.
     */
    public val events: SharedFlow<ViewModelEvent> = _events.asSharedFlow()

    /**
     * Fired an event into the view model events flow.
     *
     * @param event The event to fire
     */
    protected suspend fun fireEvent(event: ViewModelEvent) {
        _events.emit(event)
    }

    //
    // State configuring for view models
    //

    /**
     * List of all current ui stated registered.
     */
    protected val uiStates: HashMap<Pair<KClass<*>, *>, MutableStateFlow<*>> = hashMapOf()

    /**
     * Registers a new ui state.
     *
     * @param initialValue The initial value of the newly registered ui state.
     * @param key The key used to uniquely associate the type with the state flow, if multiple states of the same type are present. [Unit] is disallowed.
     * @param Data The type of which to create a new ui state for
     * @return The newly created ui state flow
     */
    protected inline fun <reified Data> registerUiState(
        initialValue: Data,
        key: Any
    ): MutableStateFlow<Data> {
        val stateFlow = MutableStateFlow(initialValue)

        val dataClass = Data::class
        uiStates[Pair(dataClass, key)] = stateFlow

        return stateFlow
    }

    /**
     * Registers a new ui state.
     *
     * @param initialValue The initial value of the newly registered ui state.
     * @param Data The type of which to create a new ui state for
     * @return The newly created ui state flow
     */
    protected inline fun <reified Data : Any> registerUiState(initialValue: Data): MutableStateFlow<Data> =
        registerUiState(initialValue, Unit)

    /**
     * Retrieves the ui state of the specified type and key.
     *
     * @param key The key used to uniquely identify the state flow, if multiple state flows of the same type exist.
     */
    @Suppress("UNCHECKED_CAST")
    protected inline fun <reified Data : Any> uiState(key: Any): MutableStateFlow<Data> {
        val dataClass = Data::class
        val stateFlow = uiStates[Pair(dataClass, key)]

        return stateFlow as MutableStateFlow<Data>
    }

    /**
     * Retrieves the ui state of the specified type.
     */
    protected inline fun <reified Data : Any> uiState(): MutableStateFlow<Data> =
        uiState<Data>(Unit)


    /**
     * Updates the ui state of specified type with the given block.
     *
     * @param block The lambda creating the new element
     */
    protected inline fun <reified Data : Any> updateUiState(block: (Data) -> Data) {
        val stateFlow = uiState<Data>()

        stateFlow.update(block)
    }

}

/**
 * A composable which is supposed to wrap a composable that acts as a screen, and is controlled by a [AppViewModel].
 */
@Composable
public inline fun <ScreenEvent : UIE, Event : VME, reified ViewModel : AppViewModel<ScreenEvent, Event>> ViewModelScope(
    noinline onViewModelEvent: suspend ViewModel.(Event) -> Unit = {},
    crossinline content: @Composable (ViewModel) -> Unit
) {
    val viewModel = koinViewModel<ViewModel>()

    LaunchedEffect(Unit) {
        viewModel.events
            .collectLatest { viewModel.onViewModelEvent(it) }
    }

    content(viewModel)
}