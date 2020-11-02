package com.example.multythread

open class ExampleHandler(
    val looper: ExampleLooper = ExampleLooper.getLooper()
) {

    open fun handleMessage(message: ExampleMessage) {}

    fun post(runnable: Runnable) {
        val message = ExampleMessage()
        message.target = this@ExampleHandler
        message.callback = runnable

        val queue = looper.queue
        queue.add(message)
    }

    fun sendEmptyMessage(what: Int) {
        val message = ExampleMessage()
        message.target = this
        message.what = what

        val queue = looper.queue
        queue.add(message)
    }

    fun dispatchMessage(message: ExampleMessage) {
        if (message.callback != null) {
            message.callback?.run()
        } else {
            handleMessage(message)
        }
    }
}