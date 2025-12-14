package com.example.ui.main

import com.example.domain.model.SearchedPhoto
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.common.mvi.ViewState

internal data class MainViewState(
    val isLoading: Boolean = false,
    val error: ContentErrorConfig?,
    val searchQuery: String,
    val lastSearch: String,
    val searchedPhotos: List<SearchedPhoto>,
    val searchHistory: List<String>,
) : ViewState