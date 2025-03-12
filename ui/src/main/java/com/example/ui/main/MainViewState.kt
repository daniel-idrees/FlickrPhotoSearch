package com.example.ui.main

import androidx.annotation.StringRes
import com.example.domain.model.Photo
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.common.mvi.ViewState

internal data class MainViewState(
    val isLoading: Boolean = false,
    val error: ContentErrorConfig? = null,
    val searchQuery: String = "",
    val lastSearch: String = "",
    val photoList: List<Photo> = emptyList(),
    @StringRes val searchResultTitleRes : Int = 0,
    val searchHistory: ArrayDeque<String> = ArrayDeque(),
) : ViewState