package com.example.ui.main.bottomtabs

import com.example.ui.common.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor() : MviViewModel<SearchUiEvent, SearchViewState, SearchUiEffect>() {
    override fun setInitialState(): SearchViewState = SearchViewState(selectedPhoto = null)

    override fun handleEvents(event: SearchUiEvent) {
        when (event) {
            SearchUiEvent.ClearPhotoOverlay -> {
                setEffect { SearchUiEffect.HidePhotoOverlay }
                setState {
                    copy(selectedPhoto = null)
                }
            }

            is SearchUiEvent.OnPhotoClick -> {
                setState {
                    setEffect { SearchUiEffect.ShowPhotoOverlay }
                    copy(selectedPhoto = event.photo)
                }
            }
        }
    }
}