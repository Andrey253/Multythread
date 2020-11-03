package com.example.multythread

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.main_fragment.*
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

class ExampleFragment : Fragment() {

    companion object {

        const val WHAT_KEY = 1

        fun newInstance() = ExampleFragment()
    }

    private val mainHandler = object : ExampleHandler() {
        override fun handleMessage(message: ExampleMessage) {
            when (message.what) {
                WHAT_KEY -> messageView.text = "Obtain new message"
                else -> throw IllegalArgumentException("Unknown what=${message.what}")
            }
        }
    }

    private val thread = Thread {
        Thread.sleep(3000L)

        val handler = ExampleHandler(ExampleLooper.mainLooper)
        handler.post {
            messageView.text = "Post text from handler"
        }

        Thread.sleep(2000L)
        mainHandler.sendEmptyMessage(WHAT_KEY)
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
        startWork()
    }

    private fun startWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val request = OneTimeWorkRequestBuilder<ExampleWorker>()
            .setConstraints(constraints)
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(request)
    }
}