package com.example.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.example.newsapp.data.local.BookmarkDao
import com.example.newsapp.data.local.ArticleEntity
import com.example.newsapp.data.network.NewsApiService
import com.example.newsapp.data.network.dto.toDomain
import com.example.newsapp.paging.NewsPagingSource
import com.example.newsapp.domain.model.Article
import com.example.newsapp.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the NewsRepository interface that handles data operations
 * for news articles, including fetching headlines, searching articles, and managing bookmarks.
 *
 * @param api The API service for fetching news data from remote sources
 * @param dao The Data Access Object for local database operations
 */
class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApiService,
    private val dao: BookmarkDao
) : NewsRepository {

    /**
     * Retrieves a stream of headline articles using pagination.
     *
     * @return Flow of PagingData containing Article objects
     */
    override fun getHeadlinesStream(): Flow<androidx.paging.PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(api, query = null, topHeadlines = true) }
        ).flow.map { pagingData ->
            pagingData.map { dto -> dto.toDomain() }
        }
    }

    /**
     * Searches for articles based on a query string using pagination.
     *
     * @param query The search query string
     * @return Flow of PagingData containing Article objects matching the query
     */
    override fun searchArticlesStream(query: String): Flow<androidx.paging.PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(api, query = query, topHeadlines = false) }
        ).flow.map { pagingData ->
            pagingData.map { dto -> dto.toDomain() }
        }
    }

    /**
     * Bookmarks an article by saving it to the local database.
     *
     * @param article The Article object to bookmark
     */
    override suspend fun bookmarkArticle(article: Article) {
        dao.upsert(ArticleEntity.fromDomain(article))
    }

    /**
     * Removes a bookmarked article from the local database.
     *
     * @param url The URL of the article to remove from bookmarks
     */
    override suspend fun removeBookmark(url: String) {
        dao.deleteByUrl(url)
    }

    /**
     * Retrieves all bookmarked articles from the local database.
     *
     * @return Flow of List containing bookmarked Article objects
     */
    override fun getBookmarks(): Flow<List<Article>> =
        dao.getAllBookmarks().map { list -> list.map { it.toDomain() } }

    /**
     * Checks if an article is bookmarked.
     *
     * @param url The URL of the article to check
     * @return Boolean indicating whether the article is bookmarked
     */
    override suspend fun isBookmarked(url: String): Boolean = dao.isBookmarked(url)
}