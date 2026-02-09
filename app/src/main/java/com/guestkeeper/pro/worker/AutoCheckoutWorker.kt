package com.guestkeeper.pro.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.guestkeeper.pro.R
import com.guestkeeper.pro.database.AppDatabase
import com.guestkeeper.pro.model.VisitStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class AutoCheckoutWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            autoCheckoutOverdueVisits()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun autoCheckoutOverdueVisits() {
        withContext(Dispatchers.IO) {
            val database = AppDatabase.getDatabase(applicationContext)
            val currentTime = Date()

            // Get overdue visits (15 minutes past estimated departure)
            val overdueTime = Calendar.getInstance().apply {
                time = currentTime
                add(Calendar.MINUTE, -15) // 15 minutes grace period
            }.time

            val overdueVisits = database.visitDao().getOverdueVisits(overdueTime)

            overdueVisits.forEach { visit ->
                // Auto checkout
                database.visitDao().checkoutVisit(
                    visitId = visit.id,
                    departureTime = currentTime,
                    notes = "Auto-checkout: Overdue",
                    updateTime = currentTime
                )

                // Update tag status if exists
                visit.tagId?.let { tagId ->
                    database.tagDao().updateTagStatus(tagId, "AVAILABLE", null)
                }

                // Send notification
                sendOverdueNotification(visit.id, currentTime)
            }
        }
    }

    private fun sendOverdueNotification(visitId: Long, checkoutTime: Date) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "auto_checkout_channel",
                "Auto Checkout",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(
            applicationContext,
            "auto_checkout_channel"
        )
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Visitor Auto-Checkout")
            .setContentText("Visit #$visitId was automatically checked out at $checkoutTime")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(visitId.toInt(), notification)
    }
}

