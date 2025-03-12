package com.example.ui.main

import androidx.lifecycle.viewModelScope
import com.example.domain.PhotoSearchResult
import com.example.domain.usecase.GetPhotoListUseCase
import com.example.ui.R
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.common.mvi.MviViewModel
import com.example.ui.main.bottomtabs.screen.config.BottomBarScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val getPhotoListUseCase: GetPhotoListUseCase
) : MviViewModel<MainUiEvent, MainViewState, MainUiEffect>() {

    override fun setInitialState(): MainViewState = MainViewState(
        isLoading = false,
        error = null
    )

    override fun handleEvents(event: MainUiEvent) {
        when (event) {
            is MainUiEvent.RequestSearch -> doOnSearchRequest(event)
            is MainUiEvent.OnPhotoClicked -> {
                // for analytics or detail fetch
            }

            is MainUiEvent.OnNavigateBackRequest -> setEffect { MainUiEffect.Navigation.Pop(event.fromScreen) }
            is MainUiEvent.OnSearchQueryChange -> setState {
                copy(
                    error = null,
                    searchQuery = event.query
                )
            }

            MainUiEvent.ClearSearchHistory -> setState { copy(searchHistory = ArrayDeque()) }
            is MainUiEvent.RemoveSearchHistory -> {
                setState {
                    val newSearchHistory = ArrayDeque(searchHistory).apply {
                        removeAt(event.index)
                    }
                    copy(
                        searchHistory = newSearchHistory
                    )
                }
            }

            is MainUiEvent.OnSearchHistoryItemSelected -> {
                if (event.fromScreen != BottomBarScreen.Search) {
                    switchToSearchScreen()
                }
                search(searchQuery = event.searchQuery)
            }
        }
    }

    private fun doOnSearchRequest(event: MainUiEvent.RequestSearch) {
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
            getPhotoListUseCase(searchQuery).collect { result ->
                when (result) {
                    is PhotoSearchResult.Success -> {
                        setState {
                            copy(
                                error = null,
                                isLoading = false,
                                photoList = result.photos,
                                searchResultTitleRes = R.string.main_view_model_success_result_title,
                            )
                        }
                    }

                    is PhotoSearchResult.Error.SearchFailed -> {
                        setState {
                            copy(
                                isLoading = false,
                                photoList = emptyList(),
                                error = ContentErrorConfig(
                                    errorTitleRes = ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_search_failed_error_title),
                                    errorSubTitleRes = ContentErrorConfig.ErrorMessage.Text(result.errorMessage),
                                    onRetry = { search(searchQuery) },
                                )
                            )
                        }
                    }

                    PhotoSearchResult.Empty -> setState {
                        copy(
                            isLoading = false,
                            photoList = emptyList(),
                            error = ContentErrorConfig(
                                errorTitleRes = ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_empty_error_title),
                                errorSubTitleRes = ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_empty_error_sub_title),
                                onRetry = { search(searchQuery) },
                            )
                        )
                    }

                    PhotoSearchResult.Error.Generic -> {
                        setState {
                            copy(
                                isLoading = false,
                                photoList = emptyList(),
                                error = ContentErrorConfig(
                                    errorTitleRes = ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_generic_error_title),
                                    errorSubTitleRes = ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_generic_error_sub_title),
                                    onRetry = { search(searchQuery) },
                                )
                            )
                        }
                    }

                    PhotoSearchResult.Error.NoInternetConnection -> {
                        setState {
                            copy(
                                isLoading = false,
                                photoList = emptyList(),
                                error = ContentErrorConfig(
                                    errorTitleRes = ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_no_internet_connection_error_title),
                                    errorSubTitleRes = ContentErrorConfig.ErrorMessage.Resource(R.string.main_view_model_no_internet_connection_error_sub_title),
                                    onRetry = { search(searchQuery) },
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getUpdatedSearchHistory(searchQuery: String): ArrayDeque<String> {
        val searchHistory = viewState.value.searchHistory
        searchHistory.addFirst(searchQuery)
        return searchHistory
    }
}
