package com.example.multythread

import kotlinx.coroutines.*
import java.util.concurrent.ArrayBlockingQueue

class ExampleLooper {

    companion object {
        lateinit var mainLooper: ExampleLooper
        private val threadLocal = ThreadLocal<ExampleLooper>()

        fun prepare() {
            val newLooper = ExampleLooper()
            threadLocal.set(newLooper)
        }

        fun prepareMainLooper() {
            prepare()
            mainLooper = getLooper()
        }

        fun getLooper(): ExampleLooper = threadLocal.get()
            ?: throw RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.")

        fun loop() {
            val looper = getLooper()
            val queue = looper.queue

            while (true) {
                val message = queue.poll() ?: continue
                message.target?.dispatchMessage(message)
            }
        }

        fun mainLoop() {
            val looper = getLooper()
            val queue = looper.queue

            CoroutineScope(Dispatchers.Main).launch {
                while (true) {
                    delay(100)
                    val message = queue.poll() ?: continue
                    message.target?.dispatchMessage(message)
                }
            }
        }
    }

    val queue = ArrayBlockingQueue<ExampleMessage>(10)
}