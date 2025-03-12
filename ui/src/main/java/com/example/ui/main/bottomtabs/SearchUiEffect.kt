package com.example.ui.main.bottomtabs

import com.example.ui.common.mvi.ViewSideEffect

internal sealed interface SearchUiEffect : ViewSideEffect {
    data object ShowPhotoOverlay : SearchUiEffect
    data object HidePhotoOverlay : SearchUiEffect
}