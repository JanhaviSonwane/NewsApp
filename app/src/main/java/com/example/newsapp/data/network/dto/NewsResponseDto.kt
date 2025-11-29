package com.example.newsapp.data.network.dto

/**
 * Data class representing the response from a news API
 * Contains general information about the news articles and the list of articles
 *
 * @property status The status of the API response (e.g., "ok")
 * @property totalResults The total number of articles available
 * @property articles A list of ArticleDto objects containing the actual news articles
 */
data class NewsResponseDto(
    val status: String?,    // Status of the API response, can be null
    val totalResults: Int?, // Total count of articles available, can be null
    val articles: List<ArticleDto>? // List of articles, can be null
)

data class ArticleDto(
    val source: SourceDto?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)

data class SourceDto(val id: String?, val name: String?)