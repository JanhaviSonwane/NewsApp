package com.example.newsapp.data.network

import com.example.newsapp.data.network.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * An interface defining the API service for fetching news data.
 * This interface uses Retrofit annotations to define the endpoints and their query parameters.
 */
interface NewsApiService {
    /**
     * Searches for all news articles matching the given query.
     *
     * @param query The search keyword to filter news articles.
     * @param page The page number of the results to retrieve.
     * @param pageSize The number of results to return per page.
     * @return A NewsResponseDto containing the search results.
     */
    @GET("everything")
    suspend fun searchEverything(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): NewsResponseDto

    /**
     * Fetches the top headlines for a specified country.
     * Defaults to returning headlines for the United States if no country is specified.
     *
     * @param country The two-letter ISO country code to get headlines for. Defaults to "us".
     * @param page The page number of the results to retrieve.
     * @param pageSize The number of results to return per page.
     * @return A NewsResponseDto containing the top headlines.
     */
    @GET("top-headlines")
    suspend fun topHeadlines(
        @Query("country") country: String = "us",
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): NewsResponseDto
}