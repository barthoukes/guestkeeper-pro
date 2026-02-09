package com.guestkeeper.pro

import android.app.Application
import android.content.Context
import androidx.work.*
import com.guestkeeper.pro.database.AppDatabase
import com.guestkeeper.pro.worker.AutoCheckoutWorker
import com.guestkeeper.pro.worker.DailyBackupWorker
import com.guestkeeper.pro.worker.NotificationWorker
import java.util.concurrent.TimeUnit

class GuestKeeperApplication : Application() {

    companion object {
        lateinit var instance: GuestKeeperApplication
            private set
    }

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
        setupWorkers()
    }

    private fun setupWorkers() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        // Daily backup at 2 AM
        val backupRequest = PeriodicWorkRequestBuilder<DailyBackupWorker>(
            24, TimeUnit.HOURS,
            1, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInitialDelay(2, TimeUnit.HOURS)
            .build()

        // Auto checkout check every 30 minutes
        val checkoutRequest = PeriodicWorkRequestBuilder<AutoCheckoutWorker>(
            30, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        // Notification check every 15 minutes
        val notificationRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_backup",
            ExistingPeriodicWorkPolicy.KEEP,
            backupRequest
        )

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "auto_checkout",
            ExistingPeriodicWorkPolicy.KEEP,
            checkoutRequest
        )

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "notifications",
            ExistingPeriodicWorkPolicy.KEEP,
            notificationRequest
        )
    }
}

