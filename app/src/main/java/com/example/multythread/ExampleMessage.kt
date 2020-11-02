package com.example.multythread

class ExampleMessage {

    var what: Int? = null
    var target: ExampleHandler? = null
    var callback: Runnable? = null
}