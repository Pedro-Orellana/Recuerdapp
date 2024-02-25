package com.pedroapps.recuerdapp.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.pedroapps.recuerdapp.R
import com.pedroapps.recuerdapp.utils.NOTIFICATION_CHANNEL_ID

class RecuerdappNotificationReceiver: BroadcastReceiver() {

    //TODO(This is a test broadcast receiver, modify the code so that it works correctly in production)
    override fun onReceive(context: Context?, intent: Intent?) {

        if (context != null && intent != null) {

            val manager = context.getSystemService(NotificationManager::class.java)

            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("After 10 seconds!")
                .setContentText("You received this notification 10 seconds after clicking the button")
                .setSmallIcon(R.drawable.ic_launcher_foreground)

            manager.notify(2, notification.build())

        }
    }
}