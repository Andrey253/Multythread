package com.example.multythread

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.example.calendar.service.ExampleService
import kotlinx.android.synthetic.main.main_fragment.*
import java.lang.IllegalArgumentException


class ExampleFragment : Fragment() {

    companion object {

        const val WHAT_KEY = 1

        fun newInstance() = ExampleFragment()
    }
    init{
         var enableTread = true
         var count = 0
    }

    var count = 0
    var enableTread: Boolean = true

//    private val notificationManager= getSystemService(context!!,MainActivity::class.java ) as NotificationManager
    // не получилось из фрагмента сделать Push

    private val mainHandler = object : ExampleHandler() {
        override fun handleMessage(message: ExampleMessage) {
            when (message.what) {
                WHAT_KEY ->{
                    messageView?.text = "Obtain new message"
                }
                else -> throw IllegalArgumentException("Unknown what=${message.what}")
            }
        }
    }

    private val thread = Thread {
        val handler = ExampleHandler(ExampleLooper.mainLooper)

        while (count < 10000 && enableTread){

            count++

        Thread.sleep(1000L)

        handler.post {
            messageView?.text =
                    "После нажатия Back,\n" +
                    "Push появится через 3 секунды \n" +
                    "с последним показанием счетчика\n" +
                    "Счетчик = $count"
        }
    }
        //Thread.sleep(1000L)
       // mainHandler.sendEmptyMessage(WHAT_KEY)
    }

    private val exampleHandlerThread = ExampleHandlerThread()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        thread.start()

        exampleHandlerThread.start()
        exampleHandlerThread.getHandler().post {
            Log.d("ExampleHandlerThread", "Some work")
        }
        //startWork()
    }

/*
    private fun startWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val request = OneTimeWorkRequestBuilder<ExampleWorker>()
            .setConstraints(constraints)
            .setInitialDelay(7, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(request)
    }*/
}