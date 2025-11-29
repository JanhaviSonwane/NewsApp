package com.example.newsapp.di

import android.content.Context
import androidx.room.Room
import com.example.newsapp.data.local.AppDatabase
import com.example.newsapp.data.local.BookmarkDao
import com.example.newsapp.data.network.NewsApiService
import com.example.newsapp.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val apiKeyInterceptor = Interceptor { chain ->
            val original = chain.request()
            val originalUrl = original.url
            val url = originalUrl.newBuilder()
                .addQueryParameter("apiKey", Constants.API_KEY)
                .build()
            val request = original.newBuilder().url(url).build()
            chain.proceed(request)
        }
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(logging)
            .build()
    }

/**
 * Provides a Retrofit instance configured for news API requests.
 * This function is annotated with @Provides to indicate it's a Dagger provider method,
 * and @Singleton to ensure only one instance of Retrofit is created throughout the application.
 *
 * @param gson Gson instance for JSON serialization/deserialization
 * @param ok OkHttpClient for HTTP requests
 * @return A configured Retrofit instance
 */
    @Provides
    @Singleton
    fun provideNewsRetrofit(gson: Gson, ok: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)  // Set the base URL for the API
            .client(ok)  // Use the provided OkHttpClient for network requests
            .addConverterFactory(GsonConverterFactory.create(gson))  // Add Gson converter for JSON parsing
            .build()  // Build the Retrofit instance

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApiService =
        retrofit.create(NewsApiService::class.java)

/**
 * Provides a singleton instance of the AppDatabase
 * This function is annotated with @Provides to indicate it's a Dagger provider method
 * and @Singleton to ensure only one instance of the database is created throughout the application
 *
 * @param ctx The application context obtained through Dagger's @ApplicationContext qualifier
 * @return An instance of AppDatabase configured with the specified parameters
 */
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "news_db") // Creates a Room database builder with context, class type, and database name
            .fallbackToDestructiveMigration() // Allows database to be recreated if migration is needed
            .build() // Builds and returns the database instance

/**
 * Provides a BookmarkDao instance for dependency injection.
 * This function is annotated with @Provides, indicating it's a Dagger provider method.
 *
 * @param db The AppDatabase instance from which to obtain the BookmarkDao
 * @return A BookmarkDao instance associated with the provided database
 */
    @Provides
    fun provideBookmarkDao(db: AppDatabase): BookmarkDao = db.bookmarkDao()

/**
 * Provides an IO dispatcher for coroutine operations.
 * This function is annotated with @Provides, indicating it's part of a dependency injection setup.
 * It returns the predefined Dispatchers.IO which is optimized for disk and network operations.
 *
 * @return The IO dispatcher instance to be used for I/O-bound tasks
 */
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO  // Returns the predefined IO dispatcher for coroutine operations
}