package com.example.ui.main.bottomtabs

import com.example.ui.common.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor() : MviViewModel<SearchUiAction, SearchViewState, SearchUiEffect>() {
    override fun setInitialState(): SearchViewState = SearchViewState(selectedPhoto = null)

    override fun handleAction(event: SearchUiAction) {
        when (event) {
            SearchUiAction.ClearPhotoOverlay -> {
                setEffect { SearchUiEffect.HidePhotoOverlay }
                setState {
                    copy(selectedPhoto = null)
                }
            }

            is SearchUiAction.OnPhotoClick -> {
                setState {
                    setEffect { SearchUiEffect.ShowPhotoOverlay }
                    copy(selectedPhoto = event.photo)
                }
            }
        }
    }
}