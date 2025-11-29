package com.example.newsapp.data.network.dto

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.model.Source

/**
 * Convert ArticleDto to Article domain model
 * This is an extension function that transforms a data transfer object into a domain entity
 *
 * @return Article The converted domain entity with properly mapped fields
 */
fun ArticleDto.toDomain(): Article = Article(
    title = title.orEmpty(),          // Convert title to empty string if null
    description = description,        // Keep description as is (nullable)
    content = content,               // Direct mapping of content
    url = url.orEmpty(),             // Convert url to empty string if null
    imageUrl = urlToImage,           // Map urlToImage to imageUrl
    publishedAt = publishedAt,       // Direct mapping of publishedAt
    source = Source(source?.id, source?.name)  // Create Source with nullable id and name
)