package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var fileName:String
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        fileName=""
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createChannel(CHANNEL_ID,getString(R.string.notification_channel_name))// create notification channel
         notificationManager=ContextCompat.getSystemService(applicationContext,NotificationManager::class.java) as NotificationManager
        custom_button.setOnClickListener {



         // to select url
        if (selectedOption())
            download()
//            notificationManager.sendNotification("hello bro",this,fileName,"true")
       else
            Toast.makeText(this,"please select",Toast.LENGTH_SHORT).show()
        }


    }
    private fun selectedOption():Boolean
    {
        var isSelected = false
        val bind=binding.fr
        val radioGroup=bind.radioGroup
        when(radioGroup.checkedRadioButtonId){
            R.id.radioButton1-> {
                isSelected=true
                URL= urlLoadApp
                fileName=getString(R.string.glide_file)
            }
            R.id.radioButton2-> {
                isSelected=true
                URL= urlGide
                fileName=getString(R.string.load_app_file)
            }
            R.id.radioButton3-> {
                isSelected=true
                URL= urlRetrofit
                fileName=getString(R.string.retrofit_file)
            }
            else-> bind.customButton.animateflag()
        }

        return isSelected
    }

     private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) // if api level 26
        {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )//error because yor api less 26 change it // android 8
//                    .apply {
//                        setShowBadge(true)   // list of notifications or  dot that appeared in app icon when long press  turn it as you like it tru by default needn't turn of//
//                    }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Time for break fast , channel created"

            val notificationManager =
                ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    ///////////////////////
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

                        binding.fr.customButton.animateflag()

            if (id == downloadID) {

                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val query = DownloadManager.Query()
                query.setFilterById(id)
                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(
                        cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    )
                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            notificationManager.sendNotification("Download SUCCESSFUL",context!!,fileName,"SUCCESSFUL")
                        }
                        DownloadManager.STATUS_FAILED -> {
                            notificationManager.sendNotification("Download FAILED ",context!!,fileName,"FAILED")
                        }
                    }
                }
            }
        }

    }
    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private var URL =""
        private const val urlLoadApp =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val urlGide =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val urlRetrofit =
            "https://github.com/square/retrofit/archive/master.zip"
        const val CHANNEL_ID = "channelId"
    }

}
