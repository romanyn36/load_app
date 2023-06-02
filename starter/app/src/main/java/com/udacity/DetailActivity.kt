package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityDetailBinding

import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {
lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_detail)
        setSupportActionBar(binding.toolbar)
        val notificationManager=
            ContextCompat.getSystemService(applicationContext,NotificationManager::class.java) as NotificationManager
        notificationManager.cancelAll()
        val name=intent.getStringExtra("NAME")
        val status=intent.getStringExtra("STATUS")
        binding.fr.fileName.text=name
        binding.fr.status.text=status
        if (status=="FAILED")
            binding.fr.status.setTextColor(Color.RED)
        back.setOnClickListener {
            finish()
        }

    }

}
