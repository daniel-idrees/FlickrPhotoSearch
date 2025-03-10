package com.example.ui.main

import androidx.lifecycle.viewModelScope
import com.example.domain.PhotoSearchResult
import com.example.domain.usecase.GetPhotoListUseCase
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
        title = "Flickr Photo Search"
    )

    override fun handleEvents(event: MainUiEvent) {
        when (event) {
            is MainUiEvent.OnSearchRequest -> doOnSearchRequest(event)

            is MainUiEvent.OnPhotoItemClicked -> {
                // TODO Do something with the photo item
            }

            is MainUiEvent.OnBackPressed -> setEffect { MainUiEffect.Navigation.Pop(event.fromScreen) }
            is MainUiEvent.OnSearchTextChange -> setState {
                copy(
                    error = null,
                    searchQuery = event.searchQuery
                )
            }

            MainUiEvent.OnClearAllButtonClick -> setState { copy(searchHistory = ArrayDeque()) }
            is MainUiEvent.DeleteFromSearchHistory -> {
                setState {
                    val newSearchHistory = ArrayDeque(searchHistory).apply {
                        removeAt(event.index)
                    }
                    copy(
                        searchHistory = newSearchHistory
                    )
                }
            }

            is MainUiEvent.OnSearchHistoryItemClicked -> {
                if (event.fromScreen != BottomBarScreen.Search) {
                    switchToSearchScreen()
                }
                search(searchQuery = event.searchQuery)
            }
        }
    }

    private fun doOnSearchRequest(event: MainUiEvent.OnSearchRequest) {
        if (event.searchQuery.isEmpty() || event.searchQuery.isBlank()) {
            setEffect {
                MainUiEffect.ShowEmptyTextError("Enter something to search")
            }
        } else {
            if (event.fromScreen != BottomBarScreen.Search) {
                switchToSearchScreen()
            }
            search(searchQuery = event.searchQuery)
        }
    }

    private fun switchToSearchScreen() {
        setEffect { MainUiEffect.Navigation.SwitchScreen(BottomBarScreen.Search) }
    }

    private fun search(searchQuery: String) {
        setState {
            copy(
                isLoading = true,
                searchQuery = searchQuery,
                error = null,
                searchHistory = getUpdatedSearchHistory(searchQuery),
            )
        }

        viewModelScope.launch {
            getPhotoListUseCase(searchQuery).collect { result ->
                when (result) {
                    PhotoSearchResult.Empty -> setState {
                        copy(
                            subtitle = "",
                            isLoading = false,
                            error = ContentErrorConfig(
                                errorTitle = "Nothing found...",
                                errorSubTitle = "Unable to find anything. Try something else",
                                onRetry = { search(searchQuery) },
                            )
                        )
                    }

                    PhotoSearchResult.Error.NoResult -> {
                        setState {
                            copy(
                                subtitle = "",
                                isLoading = false,
                                error = ContentErrorConfig(
                                    errorTitle = "Oops...",
                                    errorSubTitle = "Result Unavailable.",
                                    onRetry = { search(searchQuery) },
                                )
                            )
                        }
                    }

                    PhotoSearchResult.Error.Generic -> {
                        setState {
                            copy(
                                subtitle = "",
                                isLoading = false,
                                error = ContentErrorConfig(
                                    errorTitle = "Oops...",
                                    errorSubTitle = "Something went wrong.",
                                    onRetry = { search(searchQuery) },
                                )
                            )
                        }
                    }

                    PhotoSearchResult.Error.NoInternetConnection -> {
                        setState {
                            copy(
                                subtitle = "",
                                isLoading = false,
                                error = ContentErrorConfig(
                                    errorTitle = "Oops...",
                                    errorSubTitle = "Please check your Internet connection.",
                                    onRetry = { search(searchQuery) },
                                )
                            )
                        }
                    }


                    is PhotoSearchResult.Success -> {
                        setState {
                            copy(
                                error = null,
                                isLoading = false,
                                photoList = result.photos,
                                searchResultTitle = "Showing results for \"$searchQuery\"",
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
