package com.example.newsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a bookmarked article in the database.
 *
 * This class is annotated with @Entity to indicate that it's a room database entity.
 * It defines the structure of the "bookmarks" table in the database.
 *
 * @param url The primary key of the entity, representing the URL of the article
 * @param title The title of the article
 * @param description The description of the article (nullable)
 * @param content The content of the article (nullable)
 * @param imageUrl The URL of the image associated with the article (nullable)
 * @param publishedAt The publication date of the article (nullable)
 * @param sourceName The name of the source of the article (nullable)
 */
@Entity(tableName = "bookmarks")
data class ArticleEntity(
    @PrimaryKey val url: String,  // Primary key field, uniquely identifies each article
    val title: String,           // Title of the article
    val description: String?,    // Optional description of the article
    val content: String?,        // Optional content of the article
    val imageUrl: String?,       // Optional URL for the article's image
    val publishedAt: String?,    // Optional publication date as string
    val sourceName: String?     // Optional name of the article's source
) {
    /**
     * Companion object containing factory methods for creating instances of ArticleEntity.
     */
    companion object {
        /**
         * Creates an ArticleEntity from a domain model Article object.
         *
         * @param domain The domain model Article to convert from
         * @return A new ArticleEntity instance with values from the domain model
         */
        fun fromDomain(domain: com.example.newsapp.domain.model.Article) = ArticleEntity(
            url = domain.url,
            title = domain.title,
            description = domain.description,
            content = domain.content,
            imageUrl = domain.imageUrl,
            publishedAt = domain.publishedAt,
            sourceName = domain.source?.name
        )
    }

    /**
     * Converts this ArticleEntity to a domain model Article object.
     *
     * @return A new domain model Article instance with values from this entity
     */
    fun toDomain() = com.example.newsapp.domain.model.Article(
        title = title,
        description = description,
        content = content,
        url = url,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        source = com.example.newsapp.domain.model.Source(null, sourceName)
    )
}