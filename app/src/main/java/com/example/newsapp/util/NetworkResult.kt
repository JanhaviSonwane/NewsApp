package com.example.newsapp.util

/**
 * A sealed class representing the result of a network operation.
 * This class is used to handle different states of network requests:
 * - Loading: indicates that the network request is in progress
 * - Success: indicates that the network request was successful and contains data
 * - Error: indicates that the network request failed and contains an error
 *
 * @param T The type of data that will be returned in case of success
 */
sealed class NetworkResult<out T> {
    /**
     * Represents a loading state.
     * This is used when the network request is in progress.
     * It doesn't hold any data as indicated by Nothing type parameter.
     */
    object Loading : NetworkResult<Nothing>()
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val throwable: Throwable) : NetworkResult<Nothing>()
}