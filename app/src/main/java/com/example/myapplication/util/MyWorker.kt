package com.example.myapplication.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.R
import com.example.myapplication.ui.main.MainActivity
import java.time.OffsetDateTime.from
import java.util.Date.from
import java.util.GregorianCalendar.from

class MyWorker(context: Context, workerParameters:WorkerParameters):Worker(context,workerParameters) {

   companion object{
       const val ChHNNEL_ID = "channel_id"
       const val NOTIFICATION_ID=1
   }
    override fun doWork(): Result {
        Log.d("do work success","doWork:Success function called")
        showNotification()
        return Result.success()
    }

    private fun showNotification() {

        val intent=Intent(applicationContext,MainActivity::class.java).apply {
            flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent=PendingIntent.getActivity(
            applicationContext,
            0,intent,0
        )

        val notification=NotificationCompat.Builder(
            applicationContext,
            ChHNNEL_ID
        )


            .setSmallIcon(R.drawable.business_3d_icon_24)
                .setContentTitle("Chat App")
                .setContentText("Welcome, you can register now")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelName= "Channel Name"
            val mChannelDescription="Channel Description"
            val ChannelImportance= NotificationManager.IMPORTANCE_DEFAULT
            val channel= NotificationChannel(ChHNNEL_ID,channelName, ChannelImportance).apply {
                description=mChannelDescription
            }

            val notificationManager=
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        with(NotificationManagerCompat.from(applicationContext)){
           notify(NOTIFICATION_ID, notification.build())
        }
        }



    }
