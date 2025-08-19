package com.juank.shopapp

import android.app.Application
import com.juank.shopapp.data.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class AppInject: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AppInject)
            androidLogger()
        }
    }
}