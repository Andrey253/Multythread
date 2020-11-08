package com.example.multythread

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.example.calendar.service.ExampleService

class MainActivity : AppCompatActivity() {

	private val instance: ExampleFragment = ExampleFragment.newInstance()

	private val notificationManager: NotificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)

		if (savedInstanceState == null) {

			supportFragmentManager.beginTransaction()
				.replace(R.id.container, instance)
				.commitNow()
		}
	}

	override fun onBackPressed() {

		super.onBackPressed()
		println("mytag onBackPressed")
		instance.enableTread = false

		createNotificationChannel()

		val handler = Handler(Looper.getMainLooper())

		Thread.sleep(3000L)

		startNotifications(handler)
	}

	private fun startNotifications(handler: Handler) {

			val notification = buildNotification(0)
			handler.post {
				notificationManager.notify(ExampleService.REQUEST_CODE, notification)
			}
	}

	private fun buildNotification(count: Int): Notification {
		val activityIntent = Intent(this, MainActivity::class.java)
		val pendingIntent = PendingIntent.getActivity(this, ExampleService.REQUEST_CODE, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT)
		return NotificationCompat.Builder(this, ExampleService.DEFAULT_CHANNEL_ID)
				.setContentIntent(pendingIntent)
				.setSmallIcon(R.drawable.ic_launcher_background)
				.setContentTitle("Последнее значение счетчика")
				.setContentText("Count = ${instance.count}")
				.setNotificationSilent()
				.build()
	}

	private fun createNotificationChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val name = "channel"
			val descriptionText = "default channel"
			val importance = NotificationManager.IMPORTANCE_DEFAULT
			val channel = NotificationChannel(ExampleService.DEFAULT_CHANNEL_ID, name, importance).apply {
				description = descriptionText
			}
			val notificationManager: NotificationManager =
					getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			notificationManager.createNotificationChannel(channel)
		}
	}
}