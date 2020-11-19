package com.panicdev.panic.common

import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.os.Handler

open class CommonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = this.applicationContext
        applicationHandler = Handler(this.mainLooper)

    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun registerComponentCallbacks(callback: ComponentCallbacks) {
        super.registerComponentCallbacks(callback)
    }

    override fun unregisterComponentCallbacks(callback: ComponentCallbacks) {
        super.unregisterComponentCallbacks(callback)
    }

    override fun registerActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) {
        super.registerActivityLifecycleCallbacks(callback)
    }

    override fun unregisterActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) {
        super.unregisterActivityLifecycleCallbacks(callback)
    }

    override fun registerOnProvideAssistDataListener(callback: OnProvideAssistDataListener) {
        super.registerOnProvideAssistDataListener(callback)
    }

    override fun unregisterOnProvideAssistDataListener(callback: OnProvideAssistDataListener) {
        super.unregisterOnProvideAssistDataListener(callback)
    }

    companion object {
        lateinit var appContext : Context
        lateinit var applicationHandler: Handler
    }
}
