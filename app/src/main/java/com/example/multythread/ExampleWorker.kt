package com.example.multythread

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class ExampleWorker(appContext: Context,
                    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {

        //Some work
        Thread.sleep(2000)

        return Result.success()
    }
}