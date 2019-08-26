package com.bayuirfan.latihan.customnotif

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.RemoteInput
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat


class NotificationService: IntentService("NotificationService") {
    companion object {
        private const val KEY_REPLY = "key_reply_message"
        const val REPLY_ACTION = "com.bayuirfan.latihan.customnotif.REPLY_ACTION"
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"

        fun getReplyMessage(intent: Intent?): CharSequence?{
            val remoteInput = RemoteInput.getResultsFromIntent(intent)
            if (remoteInput != null){
                return remoteInput.getCharSequence(KEY_REPLY)
            }
            return null
        }
    }

    private val notificationId = 1
    private val messageId = 123

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null){
            showNotification()
        }
    }

    private fun showNotification(){
        val replyLabel = getString(R.string.notif_action_reply)
        val remoteInput = RemoteInput.Builder(KEY_REPLY)
            .setLabel(replyLabel)
            .build()

        val replyAction = NotificationCompat.Action.Builder(
            R.drawable.ic_reply, replyLabel, getReplayPendingIntent())
            .addRemoteInput(remoteInput)
            .setAllowGeneratedReplies(true)
            .build()

        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(getString(R.string.notif_title))
            .setContentText(getString(R.string.notif_content))
            .setShowWhen(true)
            .addAction(replyAction)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            mBuilder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()
        notificationManager.notify(notificationId, notification)
    }

    private fun getReplayPendingIntent() : PendingIntent?{
        val intent: Intent
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent = NotificationBroadcastReceiver.getReplyMessageIntent(this, notificationId, messageId)

            PendingIntent.getBroadcast(applicationContext, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            intent = ReplyActivity.getReplyMessageIntent(this, notificationId, messageId)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
}