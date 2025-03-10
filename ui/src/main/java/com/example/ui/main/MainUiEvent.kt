package com.example.ui.main

import com.example.domain.model.PhotoItem
import com.example.ui.common.mvi.ViewEvent
import com.example.ui.main.bottomtabs.screen.BottomBarScreen

internal sealed interface MainUiEvent : ViewEvent {
    data class OnBackPressed(val fromScreen: BottomBarScreen) : MainUiEvent
    data object OnClearAllButtonClick : MainUiEvent
    data class OnSearchRequest(val searchQuery: String, val fromScreen: BottomBarScreen) : MainUiEvent
    data class OnPhotoItemClicked(val photoItem: PhotoItem) : MainUiEvent
    data class OnSearchTextChange(val searchQuery: String) :
        MainUiEvent
    data class DeleteFromSearchHistory(val index: Int) : MainUiEvent
    data class OnSearchHistoryItemClicked(val searchQuery: String, val fromScreen: BottomBarScreen) : MainUiEvent
}