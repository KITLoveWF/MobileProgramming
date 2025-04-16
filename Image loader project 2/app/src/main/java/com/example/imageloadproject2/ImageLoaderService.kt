package com.example.imageloadproject2

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.app.*
import android.util.Log

class ImageLoaderService : Service() {

    private val handler = Handler()
    private lateinit var runnable: Runnable

    companion object {
        const val CHANNEL_ID = "ImageLoaderServiceChannel"
        const val NOTIFICATION_ID = 1
        const val INTERVAL: Long = 60 * 1000 // 1 phút
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification("Image Loader Service is running")
        Log.d("ImageLoaderService", "Service started with startForeground")
        startForeground(NOTIFICATION_ID, notification)

        runnable = object : Runnable {
            override fun run() {
                val updatedNotification = createNotification("Image Loader Service is still running")
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(NOTIFICATION_ID, updatedNotification)

                handler.postDelayed(this, INTERVAL)
            }
        }

        handler.postDelayed(runnable, INTERVAL)

        return START_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(message: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        Log.d("ImageLoaderService", "🛎️ Notification created: $message")
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Image Loader")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Image Loader Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}