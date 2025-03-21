package com.bm.arcgis_sample.domain.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class AppController : Application() {
    companion object{
        lateinit var instance: AppController
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}