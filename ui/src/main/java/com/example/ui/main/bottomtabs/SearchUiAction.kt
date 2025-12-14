package com.example.ui.main.bottomtabs

import com.example.domain.model.SearchedPhoto
import com.example.ui.common.mvi.ViewAction

internal sealed interface SearchUiAction : ViewAction {
    data class OnPhotoClick(val photo: SearchedPhoto) : SearchUiAction
    data object ClearPhotoOverlay : SearchUiAction
}