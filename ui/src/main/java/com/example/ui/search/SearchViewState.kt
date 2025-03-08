package com.example.ui.search

import com.example.domain.model.PhotoItem
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.common.mvi.ViewState

internal data class SearchViewState(
    val isLoading: Boolean = false,
    val error: ContentErrorConfig? = null,
    val title: String = "",
    val subtitle: String = "",
    val searchQuery: String = "",
    val photoList: List<PhotoItem> = emptyList(),
    val isResultListEmpty: Boolean = false,
    val searchHistory: ArrayDeque<String> = ArrayDeque()
) : ViewState