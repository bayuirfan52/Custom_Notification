package com.bayuirfan.latihan.customnotif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.bayuirfan.latihan.customnotif.NotificationService.Companion.CHANNEL_ID
import com.bayuirfan.latihan.customnotif.NotificationService.Companion.CHANNEL_NAME
import com.bayuirfan.latihan.customnotif.NotificationService.Companion.REPLY_ACTION
import kotlinx.android.synthetic.main.activity_reply.*

class ReplyActivity : AppCompatActivity() {
    companion object {
        private const val KEY_MESSAGE_ID = "key_message_id"
        private const val KEY_NOTIFY_ID = "key_notify_id"

        fun getReplyMessageIntent(context: Context, notifyId: Int, messageId: Int): Intent{
            val intent = Intent(context, ReplyActivity::class.java)
            intent.setAction(REPLY_ACTION)
            intent.putExtra(KEY_MESSAGE_ID, messageId)
            intent.putExtra(KEY_NOTIFY_ID, notifyId)
            return intent
        }
    }

    private var messageId: Int = 0
    private var notifyId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)

        if (REPLY_ACTION.equals(intent.action)){
            messageId = intent.getIntExtra(KEY_MESSAGE_ID, 0)
            notifyId = intent.getIntExtra(KEY_NOTIFY_ID, 0)
        }


    }

    private fun sendMessage(notifyId: Int, messageId: Int){
        updateNotification(notifyId)

        val message = edit_reply.text.toString().trim()
        Toast.makeText(this, "MessageId : " + messageId + "\nMessage : " + message, Toast.LENGTH_SHORT).show()

        finish()
    }

    private fun updateNotification(notifyId: Int){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(getString(R.string.notif_title_sent))
            .setContentText(getString(R.string.notif_content_sent))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000,1000,1000,1000,1000)

            builder.setChannelId(CHANNEL_ID)

            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(notifyId, notification)
    }
}
