package com.example.multythread

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ExampleLooper.prepareMainLooper()
//        ExampleLooper.loop()
        ExampleLooper.exampleLoop()
    }
}