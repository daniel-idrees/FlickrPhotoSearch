package com.example.ui.main.bottomtabs

import com.example.domain.model.SearchedPhoto
import com.example.ui.common.mvi.ViewState

internal data class SearchViewState(
    val selectedPhoto: SearchedPhoto?
) : ViewState