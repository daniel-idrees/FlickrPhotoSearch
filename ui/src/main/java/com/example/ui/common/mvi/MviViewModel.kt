package com.example.ui.common.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal abstract class MviViewModel<Action : ViewAction, UiState : ViewState, Effect : ViewSideEffect> :
    ViewModel() {

    private val initialState: UiState by lazy { setInitialState() }
    abstract fun setInitialState(): UiState

    private val _viewState: MutableStateFlow<UiState> by lazy { MutableStateFlow(initialState) }
    val viewState: StateFlow<UiState> by lazy { _viewState.asStateFlow() }

    private val _action: MutableSharedFlow<Action> = MutableSharedFlow(extraBufferCapacity = 3)

    private val _effect = Channel<Effect>(capacity = Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        subscribeToActions()
    }

    fun setAction(event: Action) {
        viewModelScope.launch { _action.emit(event) }
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.update {
            newState
        }
    }

    private fun subscribeToActions() {
        viewModelScope.launch {
            _action.collect {
                handleAction(it)
            }
        }
    }

    protected abstract fun handleAction(event: Action)

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }
}