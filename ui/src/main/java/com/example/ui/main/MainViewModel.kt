package com.example.ui.main

import androidx.lifecycle.viewModelScope
import com.example.domain.PhotoSearchResult
import com.example.domain.usecase.SearchPhotosUseCase
import com.example.ui.R
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.common.content.ContentErrorConfig.ErrorMessage.Resource
import com.example.ui.common.content.ContentErrorConfig.ErrorMessage.Text
import com.example.ui.common.mvi.MviViewModel
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val searchPhotosUseCase: SearchPhotosUseCase,
) : MviViewModel<MainUiAction, MainViewState, MainUiEffect>() {

    override fun setInitialState(): MainViewState = MainViewState(
        isLoading = false,
        error = null,
        searchQuery = "",
        lastSearch = "",
        searchedPhotos = emptyList(),
        searchHistory = emptyList()
    )

    override fun handleAction(event: MainUiAction) {
        when (event) {
            is MainUiAction.RequestSearch -> doOnSearchRequest(event)
            is MainUiAction.OnNavigateBackRequest -> setEffect { MainUiEffect.Navigation.Pop(event.fromScreen) }
            is MainUiAction.OnSearchQueryChange -> setState {
                copy(
                    error = null,
                    searchQuery = event.query
                )
            }

            MainUiAction.ClearSearchHistory -> setState { copy(searchHistory = emptyList()) }
            is MainUiAction.RemoveSearchHistory -> {
                setState {
                    copy(
                        searchHistory = searchHistory.filterIndexed { index, _ -> index != event.index }
                    )
                }
            }

            is MainUiAction.OnSearchHistoryItemSelected -> {
                if (event.fromScreen != BottomBarScreen.Search) {
                    switchToSearchScreen()
                }
                setState {
                    copy(
                        searchQuery = event.searchQuery
                    )
                }
                search(searchQuery = event.searchQuery)
            }
        }
    }

    private fun doOnSearchRequest(event: MainUiAction.RequestSearch) {
        if (event.searchQuery.isEmpty() || event.searchQuery.isBlank()) {
            setEffect {
                MainUiEffect.ShowEmptyTextError(R.string.main_view_model_empty_text_error_toast_text)
            }
        } else {
            if (event.fromScreen != BottomBarScreen.Search) {
                switchToSearchScreen()
            }
            val searchQuery = event.searchQuery.trim() // remove whitespaces
            search(searchQuery = searchQuery)
        }
    }

    private fun switchToSearchScreen() {
        setEffect { MainUiEffect.Navigation.SwitchScreen(BottomBarScreen.Search) }
    }

    private fun search(searchQuery: String) {
        setState {
            copy(
                isLoading = true,
                lastSearch = searchQuery,
                error = null,
                searchHistory = getUpdatedSearchHistory(searchQuery),
            )
        }

        viewModelScope.launch {
            when (val result = searchPhotosUseCase(searchQuery)) {
                is PhotoSearchResult.Success -> {
                    setState {
                        copy(
                            error = null,
                            isLoading = false,
                            searchedPhotos = result.searchedPhotos
                        )
                    }
                }

                is PhotoSearchResult.Error -> {
                    setState {
                        copy(
                            isLoading = false,
                            searchedPhotos = emptyList(),
                            error = result.toContentErrorConfig { search(searchQuery) }
                        )
                    }
                }
            }
        }
    }

    private fun PhotoSearchResult.Error.toContentErrorConfig(onRetry: (() -> Unit)? = null): ContentErrorConfig {
        return when (this) {
            PhotoSearchResult.Error.Generic -> ContentErrorConfig(
                errorTitleRes = Resource(R.string.main_view_model_generic_error_title),
                errorSubTitleRes = Resource(R.string.main_view_model_generic_error_sub_title),
                onRetry = onRetry,
            )

            PhotoSearchResult.Error.NoInternetConnection -> ContentErrorConfig(
                errorTitleRes = Resource(R.string.main_view_model_no_internet_connection_error_title),
                errorSubTitleRes = Resource(R.string.main_view_model_no_internet_connection_error_sub_title),
                onRetry = onRetry,
            )

            is PhotoSearchResult.Error.SearchFailed -> ContentErrorConfig(
                errorTitleRes = Resource(R.string.main_view_model_search_failed_error_title),
                errorSubTitleRes = Text(this.errorMessage),
                onRetry = onRetry,
            )

            is PhotoSearchResult.Error.Empty -> ContentErrorConfig(
                errorTitleRes = Resource(R.string.main_view_model_empty_error_title),
                errorSubTitleRes = Resource(R.string.main_view_model_empty_error_sub_title),
                onRetry = onRetry,
            )
        }
    }

    private fun getUpdatedSearchHistory(searchQuery: String): List<String> {
        val searchHistory = ArrayDeque(viewState.value.searchHistory)
        searchHistory.addFirst(searchQuery)
        return searchHistory
    }
}
