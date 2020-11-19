package com.panicdev.panic.common

import android.os.Bundle
import android.util.Log
import com.panicdev.fakelotto.BuildConfig

object L {

    val TAG = "DETRIP"

    fun d(vararg value: String) {
        if (BuildConfig.DEBUG) {
            val message = value.joinToString(":")
            Log.d(TAG, message)
        }
    }

    fun i(vararg value: String) {
        if (BuildConfig.DEBUG) {
            val message = value.joinToString(":")
            Log.i(TAG, message)
        }
    }

    fun e(vararg value: String) {
        if (BuildConfig.DEBUG) {
            val message = value.joinToString(":")
            Log.e(TAG, message)
        }
    }

    fun v(vararg value: String) {
        if (BuildConfig.DEBUG) {
            val message = value.joinToString(":")
            Log.v(TAG, message)
        }
    }

    fun w(vararg value: String) {
        if (BuildConfig.DEBUG) {
            val message = value.joinToString(":")
            Log.w(TAG, message)
        }
    }
}