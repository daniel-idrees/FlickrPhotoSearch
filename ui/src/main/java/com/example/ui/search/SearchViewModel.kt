package com.example.ui.search

import androidx.lifecycle.viewModelScope
import com.example.domain.PhotoSearchResult
import com.example.domain.usecase.GetPhotoListUseCase
import com.example.ui.common.content.ContentErrorConfig
import com.example.ui.common.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    private val getPhotoListUseCase: GetPhotoListUseCase
) : MviViewModel<SearchUiEvent, SearchViewState, SearchUiEffect>() {

    override fun setInitialState(): SearchViewState = SearchViewState(
        title = "Flickr Search",
    )

    override fun handleEvents(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.OnSearchRequest -> search(searchQuery = event.searchQuery)
            is SearchUiEvent.OnItemClicked -> setEffect {
                SearchUiEffect.Navigation.SwitchScreen(
                    "detail"
                )
            }

            SearchUiEvent.OnBackPressed -> setEffect { SearchUiEffect.Navigation.Finish }
        }
    }

    private fun search(searchQuery: String) {
        setState {
            copy(
                isLoading = true,
                searchQuery = searchQuery,
                error = null,
                searchHistory = getUpdatedSearchHistory(searchQuery)
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
                                onRetry = { search(searchQuery) })
                        )
                    }

                    is PhotoSearchResult.Error -> setState {
                        copy(
                            subtitle = "",
                            isLoading = false,
                            error = ContentErrorConfig(
                                errorTitle = "Oops...",
                                errorSubTitle = result.errorMessage,
                                onRetry = { search(searchQuery) })
                        )
                    }

                    is PhotoSearchResult.Success -> setState {
                        copy(
                            error = null,
                            isLoading = false,
                            photoList = result.photos,
                            subtitle = "Result for \"$searchQuery\""
                        )
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
