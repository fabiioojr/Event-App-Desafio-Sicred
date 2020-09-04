package com.fabiojunior.eventsapp

import android.app.Application
import org.koin.core.logger.Level
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(getEventModules())
        }
    }
}