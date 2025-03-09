package com.example.ui.main

import com.example.ui.common.mvi.ViewSideEffect
import com.example.ui.main.bottomtabs.screen.BottomBarScreen

internal sealed interface MainUiEffect : ViewSideEffect {
    data class EmptyTextError(var errorMessage: String) : MainUiEffect
    sealed interface Navigation : MainUiEffect {
        data object Finish : Navigation
        data class SwitchScreen(val screenRoute: String) : Navigation
        data class Pop(val fromScreen: BottomBarScreen) : Navigation
    }
}