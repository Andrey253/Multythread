package com.example.multythread

import android.os.Looper

class ExampleHandlerThread : Thread() {

    var looper: ExampleLooper? = null
    private var handler: ExampleHandler? = null

    override fun run() {
        ExampleLooper.prepare()
        looper = ExampleLooper.getLooper()
        ExampleLooper.loop()
    }

    fun getHandler(): ExampleHandler {
        return handler ?: ExampleHandler(ExampleLooper.getLooper()).also { handler = it }
    }
}