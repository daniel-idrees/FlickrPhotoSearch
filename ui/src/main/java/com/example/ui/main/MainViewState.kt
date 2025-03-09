package com.example.ui.main

import com.example.domain.model.PhotoItem
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.common.mvi.ViewState

data class MainViewState(
    val isLoading: Boolean = false,
    val error: ContentErrorConfig? = null,
    val title: String = "",
    val subtitle: String = "",
    val searchQuery: String = "",
    val photoList: List<PhotoItem> = emptyList(),
    val searchResultTitle : String = "",
    val searchHistory: ArrayDeque<String> = ArrayDeque(),
) : ViewState