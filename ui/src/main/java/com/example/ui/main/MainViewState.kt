package com.example.ui.main

import com.example.domain.model.Photo
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.common.mvi.ViewState

internal data class MainViewState(
    val isLoading: Boolean = false,
    val error: ContentErrorConfig? ,
    val searchQuery: String ,
    val lastSearch: String ,
    val photoList: List<Photo>,
    val searchHistory: List<String>,
) : ViewState