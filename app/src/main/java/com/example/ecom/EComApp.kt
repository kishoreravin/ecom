package com.example.ecom

import android.app.Application
import com.example.ecom.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EComApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EComApp)
            modules(appModules)
        }
    }
}