package com.example.ui.main

import com.example.ui.common.mvi.ViewAction
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen

internal sealed interface MainUiAction : ViewAction {
    data class OnNavigateBackRequest(val fromScreen: BottomBarScreen) : MainUiAction
    data object ClearSearchHistory : MainUiAction
    data class RequestSearch(val searchQuery: String, val fromScreen: BottomBarScreen) : MainUiAction
    data class OnSearchQueryChange(val query: String) : MainUiAction
    data class RemoveSearchHistory(val index: Int) : MainUiAction
    data class OnSearchHistoryItemSelected(val searchQuery: String, val fromScreen: BottomBarScreen) : MainUiAction
}