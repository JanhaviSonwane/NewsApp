package com.example.newsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for bookmark operations.
 * This interface defines methods to interact with the bookmarks database table.
 */
@Dao
interface BookmarkDao {
    /**
     * Inserts a new article into the bookmarks table or replaces it if one with the same URL already exists.
     * This is a suspend function, meaning it can be called from a coroutine.
     *
     * @param article The ArticleEntity to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: ArticleEntity)

    /**
     * Deletes a bookmarked article from the database based on its URL.
     * This is a suspend function, meaning it can be called from a coroutine.
     *
     * @param url The URL of the article to be deleted.
     */
    @Query("DELETE FROM bookmarks WHERE url = :url")
    suspend fun deleteByUrl(url: String)

    /**
     * Retrieves all bookmarked articles from the database, ordered by publication date in descending order.
     * Returns a Flow that emits a new list whenever the data changes.
     *
     * @return A Flow emitting a list of ArticleEntity objects.
     */
    @Query("SELECT * FROM bookmarks ORDER BY publishedAt DESC")
    fun getAllBookmarks(): Flow<List<ArticleEntity>>

    /**
     * Checks if an article with the given URL is bookmarked.
     * This is a suspend function, meaning it can be called from a coroutine.
     *
     * @param url The URL of the article to check.
     * @return A Boolean value indicating whether the article is bookmarked.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE url = :url LIMIT 1)")
    suspend fun isBookmarked(url: String): Boolean
}