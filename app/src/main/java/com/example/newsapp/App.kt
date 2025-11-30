package com.example.newsapp

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.newsapp.util.NotificationUtil
import com.example.newsapp.work.NewsSyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

/**
 * Application class marked as the Hilt application entry point using the @HiltAndroidApp annotation.
 * This class serves as the root of the dependency injection hierarchy for the application.
 */
@HiltAndroidApp  // Marks this Application class as the Hilt application entry point
class App : Application() {

    /**
     * Called when the application is starting, before any activity, service, or receiver objects.
     * Initializes notification channels and schedules the periodic news worker.
     */
    override fun onCreate() {
        super.onCreate()
        NotificationUtil.createChannel(this)  // Create notification channel for the app
        scheduleNewsWorker()  // Schedule periodic work for news synchronization
    }

    /**
     * Schedules a periodic work request for syncing news data.
     * The work will be performed every 6 hours when the device has an active network connection.
     * Uses unique work policy to keep existing work if already scheduled.
     */
    private fun scheduleNewsWorker() {
        // Create a periodic work request that repeats every 6 hours
        val workRequest = PeriodicWorkRequestBuilder<NewsSyncWorker>(6, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)  // Require network connection
                    .build()
            )
            .build()

        // Enqueue the unique periodic work with KEEP policy to maintain existing work
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("news_sync", ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }
}