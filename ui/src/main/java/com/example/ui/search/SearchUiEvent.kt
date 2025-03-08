package com.example.ui.search

import com.example.domain.model.PhotoItem
import com.example.ui.common.mvi.ViewEvent

internal sealed interface SearchUiEvent : ViewEvent {
    data object OnBackPressed : SearchUiEvent
    data class OnSearchRequest(val searchQuery: String) : SearchUiEvent
    data class OnItemClicked(val photoItem: PhotoItem) : SearchUiEvent
}