package com.example.newsapp.di

import com.example.newsapp.data.repository.NewsRepositoryImpl
import com.example.newsapp.data.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * BindingModule is a Dagger module that provides bindings for dependency injection.
 * It is installed in the SingletonComponent, meaning its bindings will be available
 * throughout the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class BindingModule {
    /**
     * Binds the implementation of NewsRepository (NewsRepositoryImpl) to its interface (NewsRepository).
     * This allows Dagger to provide instances of NewsRepository by automatically using NewsRepositoryImpl.
     *
     * @param impl The implementation of NewsRepository
     * @return The NewsRepository interface
     */
    @Binds
    abstract fun bindRepository(impl: NewsRepositoryImpl): NewsRepository
}