package com.bayuirfan.latihan.customnotif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.bayuirfan.latihan.customnotif.NotificationService.Companion.CHANNEL_ID
import com.bayuirfan.latihan.customnotif.NotificationService.Companion.CHANNEL_NAME
import com.bayuirfan.latihan.customnotif.NotificationService.Companion.REPLY_ACTION

class NotificationBroadcastReceiver: BroadcastReceiver() {
    companion object {
        const val KEY_NOTIFICATION_ID = "key_notification_id"
        const val KEY_MESSAGE_ID = "key_message_id"

        fun getReplyMessageIntent(context: Context, notificationId: Int, message: Int): Intent{
            val intent = Intent(context, NotificationBroadcastReceiver::class.java)
            intent.action = REPLY_ACTION
            intent.putExtra(KEY_NOTIFICATION_ID, notificationId)
            intent.putExtra(KEY_MESSAGE_ID, message)

            return intent
        }
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (REPLY_ACTION == intent?.action){
            val message = NotificationService.getReplyMessage(intent)
            val messageId = intent.getIntExtra(KEY_MESSAGE_ID, 0)

            Toast.makeText(context, "Message ID : " + messageId + "\nMessage: " + message, Toast.LENGTH_SHORT).show()

            val notifyId = intent.getIntExtra(KEY_NOTIFICATION_ID, 1)
            updateNotification(context, notifyId)
        }
    }

    private fun updateNotification(context: Context, notificationId: Int){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(context.getString(R.string.notif_title))
            .setContentText(context.getString(R.string.notif_content))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

            builder.setChannelId(CHANNEL_ID)

            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManager.notify(notificationId, notification)
    }
}