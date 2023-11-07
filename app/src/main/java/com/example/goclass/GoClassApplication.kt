package com.example.goclass

import android.app.Application
import com.example.goclass.dimodules.networkModule
import com.example.goclass.dimodules.repositoryModule
import com.example.goclass.dimodules.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GoClassApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@GoClassApplication)
            modules(listOf(repositoryModule, networkModule, viewModelModule))
        }
    }
}
