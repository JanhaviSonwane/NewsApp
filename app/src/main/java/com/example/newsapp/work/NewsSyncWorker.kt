package com.example.newsapp.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.newsapp.R
import com.example.newsapp.util.NotificationUtil

class NewsSyncWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            // Create channel if not created
            NotificationUtil.createChannel(appContext)

            // Minimal demo notification: in a real worker you'd call repository to fetch latest headlines,
            // compare timestamps and notify only when new items are available
            val notification = NotificationCompat.Builder(appContext, NotificationUtil.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("News Update")
                .setContentText("New headlines are available.")
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(appContext).notify(1001, notification)

            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }
}