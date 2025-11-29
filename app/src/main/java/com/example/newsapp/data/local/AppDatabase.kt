package com.example.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database class for the application.
 * This class defines the database configuration and provides access to the DAOs.
 *
 * @param entities List of entities that belong to this database.
 * @param version The database version number.
 * @param exportSchema Whether to export the schema to a file.
 */
@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // Abstract function to get the Bookmark Data Access Object (DAO)
    // This will be implemented by Room at runtime
    abstract fun bookmarkDao(): BookmarkDao
}