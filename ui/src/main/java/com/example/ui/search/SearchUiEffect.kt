package com.example.ui.search

import com.example.ui.common.mvi.ViewSideEffect

internal sealed interface SearchUiEffect : ViewSideEffect {
    sealed interface Navigation : SearchUiEffect {
        data object Finish : Navigation
        data class SwitchScreen(val screenRoute: String) : Navigation
    }
}