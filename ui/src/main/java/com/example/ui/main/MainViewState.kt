package com.example.ui.main

import androidx.annotation.StringRes
import com.example.domain.model.Photo
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.common.mvi.ViewState

internal data class MainViewState(
    val isLoading: Boolean = false,
    val error: ContentErrorConfig? ,
    val searchQuery: String ,
    val lastSearch: String ,
    val photoList: List<Photo>,
    @StringRes val searchResultTitleRes : Int ,
    val searchHistory: ArrayDeque<String>,
) : ViewState