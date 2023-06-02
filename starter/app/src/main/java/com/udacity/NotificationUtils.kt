package com.udacity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0
fun NotificationManager.sendNotification(
    messageBody: String, applicationContext: Context,
    name:String,
    status: String
)
{
    val intentContent=Intent(applicationContext,MainActivity::class.java)
    val pendingIntentContent=PendingIntent.getActivity(applicationContext, NOTIFICATION_ID,intentContent,PendingIntent.FLAG_UPDATE_CURRENT)


    val intent=Intent(applicationContext,DetailActivity::class.java)
    intent.putExtra("NAME",name)
    intent.putExtra("STATUS",status)
    val pendingIntent=PendingIntent.getActivity(applicationContext, NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT)

val builder=NotificationCompat.Builder(applicationContext,MainActivity.CHANNEL_ID)
    .setSmallIcon(R.drawable.ic_assistant_black_24dp)
    .setContentTitle(applicationContext.getString(R.string.notification_title))
    .setContentText(messageBody)
    .setPriority(NotificationCompat.PRIORITY_HIGH)
    .setContentIntent(pendingIntentContent)
    .addAction(R.drawable.ic_assistant_black_24dp,"Details",pendingIntent)  //add action to move to details activity
notify(NOTIFICATION_ID,builder.build())
}
