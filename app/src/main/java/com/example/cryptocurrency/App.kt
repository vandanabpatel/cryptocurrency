package com.example.cryptocurrency

import android.app.Application
import android.content.Context

class App : Application() {
    private val TAG = javaClass.simpleName

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun appContext(): Context {
            return instance!!.applicationContext
        }
    }
}