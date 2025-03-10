package com.example.ui.main

import com.example.ui.common.mvi.ViewSideEffect
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen

internal sealed interface MainUiEffect : ViewSideEffect {
    data class ShowEmptyTextError(var errorMessage: String) : MainUiEffect
    sealed interface Navigation : MainUiEffect {
        data object Finish : Navigation
        data class SwitchScreen(val toScreen: BottomBarScreen) : Navigation
        data class Pop(val fromScreen: BottomBarScreen) : Navigation
    }
}