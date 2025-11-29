package com.example.newsapp.domain.model

/**
 * A data class representing an Article with various properties.
 * Data classes in Kotlin are optimized for holding data and provide
 * automatic implementations of equals(), hashCode(), toString(), and copy().
 */
data class Article(
    /**
     * The title of the article
     */
    val title: String,
    /**
     * A brief description of the article (can be null)
     */
    val description: String?,
    /**
     * The main content of the article (can be null)
     */
    val content: String?,
    /**
     * The URL where the article can be accessed
     */
    val url: String,
    /**
     * URL of the image associated with the article (can be null)
     */
    val imageUrl: String?,
    /**
     * The publication date of the article (can be null)
     */
    val publishedAt: String?,
    /**
     * The source of the article (can be null)
     */
    val source: Source?
)

data class Source(val id: String?, val name: String?)