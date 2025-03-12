package com.example.ui.main.bottomtabs

import com.example.domain.model.Photo
import com.example.ui.common.mvi.ViewEvent

internal sealed interface SearchUiEvent : ViewEvent {
    data class OnPhotoClick(val photo: Photo) : SearchUiEvent
    data object ClearPhotoOverlay : SearchUiEvent
}