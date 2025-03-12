package com.example.ui.main.bottomtabs

import com.example.ui.common.mvi.ViewSideEffect

internal sealed interface SearchUiEffect : ViewSideEffect {
    data object ShowPhotoZoomOverlay : SearchUiEffect
    data object HidePhotoZoomOverlay : SearchUiEffect
}