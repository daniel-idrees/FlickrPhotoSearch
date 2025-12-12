package com.example.ui.main.bottomtabs

import com.example.domain.model.Photo
import com.example.ui.common.mvi.ViewAction

internal sealed interface SearchUiAction : ViewAction {
    data class OnPhotoClick(val photo: Photo) : SearchUiAction
    data object ClearPhotoOverlay : SearchUiAction
}