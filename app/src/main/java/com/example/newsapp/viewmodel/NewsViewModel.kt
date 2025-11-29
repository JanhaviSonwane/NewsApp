package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * NewsViewModel is a ViewModel class that handles business logic for news-related operations.
 * It is annotated with @HiltViewModel for dependency injection using Hilt.
 *
 * @param repo The NewsRepository instance that provides data access methods.
 */
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repo: NewsRepository
) : ViewModel() {

    // Private state flow for search query input
    private val _query = MutableStateFlow<String?>(null)

    /**
     * Query flow that debounces input and only emits distinct values.
     * The debounce(400) ensures that we only process the query after 400ms of inactivity.
     * distinctUntilChanged prevents unnecessary emissions when the query hasn't changed.
     */
    @OptIn(FlowPreview::class)
    private val queryFlow = _query
        .debounce(400)
        .distinctUntilChanged()

    // ⭐ Snackbar event flow
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    val bookmarkedUrls = repo.getBookmarks()
        .map { list -> list.map { it.url }.toSet() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )

    val articlesFlow: Flow<PagingData<Article>> = queryFlow
        .flatMapLatest { q ->
            if (q.isNullOrBlank()) {
                repo.getHeadlinesStream()
            } else {
                repo.searchArticlesStream(q)
            }
        }
        .cachedIn(viewModelScope)

    fun setQuery(q: String?) {
        _query.value = q
    }

    // ⭐ Toggle bookmark + send snackbar message
    suspend fun toggleBookmark(article: Article) {
        val exists = repo.isBookmarked(article.url)

        if (exists) {
            repo.removeBookmark(article.url)
            _snackbarMessage.value = "Removed from bookmarks"
        } else {
            repo.bookmarkArticle(article)
            _snackbarMessage.value = "Bookmarked"
        }
    }

    fun getBookmarks() = repo.getBookmarks()

    // ⭐ Clear snackbar event
    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}