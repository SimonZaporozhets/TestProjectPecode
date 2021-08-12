package com.example.testproject

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class MyCustomApplication : Application() {

	private lateinit var notificationManager: NotificationManager

	override fun onCreate() {
	    super.onCreate()

		createNotificationChannel()
	}


	private fun createNotificationChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val name = getString(R.string.channel_name)
			val descriptionText = getString(R.string.channel_description)
			val importance = NotificationManager.IMPORTANCE_HIGH
			val channel = NotificationChannel(DynamicFragment.CHANNEL_ID, name, importance).apply {
				description = descriptionText
			}

			notificationManager =
				getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			notificationManager.createNotificationChannel(channel)
		}
	}

}