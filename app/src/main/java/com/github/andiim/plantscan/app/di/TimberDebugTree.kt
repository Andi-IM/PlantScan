package com.github.andiim.plantscan.app.di

import android.annotation.SuppressLint
import android.util.Log
import timber.log.Timber

class TimberDebugTree : Timber.DebugTree() {
    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // Implement your custom log output here
        // For example, you can use Android's Log class:
        when (priority) {
            Log.VERBOSE -> Log.v(tag, message, t)
            Log.DEBUG -> Log.d(tag, message, t)
            Log.INFO -> Log.i(tag, message, t)
            Log.WARN -> Log.w(tag, message, t)
            Log.ERROR -> Log.e(tag, message, t)
            else -> Log.wtf(tag, message, t)
        }
    }
}
