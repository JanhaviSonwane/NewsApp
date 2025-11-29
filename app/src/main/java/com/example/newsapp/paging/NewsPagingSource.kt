package com.example.newsapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.data.network.NewsApiService
import com.example.newsapp.data.network.dto.ArticleDto
import com.example.newsapp.util.Constants

/**
 * A PagingSource implementation for loading news articles.
 * This class handles pagination for news articles, supporting both top headlines and search queries.
 *
 * @param service The API service for fetching news data
 * @param query The search query for articles (can be null for top headlines)
 * @param topHeadlines Flag to determine if we should load top headlines (default: false)
 */
class NewsPagingSource(
    private val service: NewsApiService,
    private val query: String?,
    private val topHeadlines: Boolean = false
) : PagingSource<Int, ArticleDto>() {

    /**
     * Loads a page of news articles based on the provided parameters.
     *
     * @param params The loading parameters containing the key (page number) and load size
     * @return A LoadResult containing the loaded page data or an error if one occurred
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleDto> {
        val page = params.key ?: 1  // Default to page 1 if no key is provided
        return try {
            // Make API call based on whether we're getting top headlines or searching
            val response = if (query.isNullOrBlank()) {
                service.topHeadlines(page = page, pageSize = params.loadSize.coerceAtMost(Constants.PAGE_SIZE))
            } else {
                service.searchEverything(query = query, page = page, pageSize = params.loadSize.coerceAtMost(Constants.PAGE_SIZE))
            }
            val items = response.articles ?: emptyList()
            // Calculate next key (page number) for pagination
            val nextKey = if (items.isEmpty()) null else page + 1
            // Return the loaded page with data and pagination keys
            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            // Return error if an exception occurs during loading
            LoadResult.Error(e)
        }
    }

    /**
     * Gets the refresh key for the PagingState.
     * This key is used to determine which page to load when the data needs to be refreshed.
     *
     * @param state The current PagingState containing the loaded pages and anchor position
     * @return The key (page number) to use for refresh, or null if no refresh key can be determined
     */
    override fun getRefreshKey(state: PagingState<Int, ArticleDto>): Int? =
        state.anchorPosition?.let { anchor ->
            // Get the closest page to the anchor position
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
}