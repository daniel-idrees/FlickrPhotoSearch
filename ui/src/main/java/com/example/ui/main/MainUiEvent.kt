package com.example.ui.main

import com.example.ui.common.mvi.ViewEvent
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen

internal sealed interface MainUiEvent : ViewEvent {
    data class OnNavigateBackRequest(val fromScreen: BottomBarScreen) : MainUiEvent
    data object ClearSearchHistory : MainUiEvent
    data class RequestSearch(val searchQuery: String, val fromScreen: BottomBarScreen) : MainUiEvent
    data class OnSearchQueryChange(val query: String) : MainUiEvent
    data class RemoveSearchHistory(val index: Int) : MainUiEvent
    data class OnSearchHistoryItemSelected(val searchQuery: String, val fromScreen: BottomBarScreen) : MainUiEvent
}