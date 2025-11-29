package com.example.newsapp.data.repository

import androidx.paging.PagingData
import com.example.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

/**
 * NewsRepository interface defines the contract for data operations related to news articles.
 * This interface provides methods for fetching headlines, searching articles,
 * and managing bookmarks, all following reactive programming patterns with Flow.
 */
interface NewsRepository {
    /**
     * Retrieves a stream of paginated headline articles.
     * @return Flow emitting PagingData of Article objects representing headlines
     */
    fun getHeadlinesStream(): Flow<PagingData<Article>>
    /**
     * Searches for articles based on a query string and returns a paginated result.
     * @param query The search term to filter articles
     * @return Flow emitting PagingData of Article objects matching the search query
     */
    fun searchArticlesStream(query: String): Flow<PagingData<Article>>
    /**
     * Bookmarks an article for future reference.
     * @param article The Article object to be bookmarked
     */
    suspend fun bookmarkArticle(article: Article)
    /**
     * Removes a bookmarked article using its URL.
     * @param url The unique URL of the article to remove from bookmarks
     */
    suspend fun removeBookmark(url: String)
    /**
     * Retrieves a stream of all bookmarked articles.
     * @return Flow emitting a List of bookmarked Article objects
     */
    fun getBookmarks(): Flow<List<Article>>
    /**
     * Checks if a specific article is bookmarked.
     * @param url The unique URL of the article to check
     * @return Boolean indicating whether the article is bookmarked (true) or not (false)
     */
    suspend fun isBookmarked(url: String): Boolean
}