package com.bm.arcgis_sample.domain.di

import android.app.Application
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import com.arcgismaps.mapping.symbology.PictureMarkerSymbol
import com.bm.arcgis_sample.R
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class AppController : Application() {
    companion object {
        lateinit var instance: AppController
    }

    fun createPinSymbol(): PictureMarkerSymbol {
        //add a graphic for the tapped point
        val pinDrawable = ContextCompat.getDrawable(
            instance.applicationContext,
            R.drawable.favourites
        )
        //add a graphic for the tapped point
        val pinSymbol = PictureMarkerSymbol.createWithImage(
            pinDrawable as BitmapDrawable
        )
        pinSymbol.apply {
            // resize the dimensions of the symbol
            width = 50f
            height = 50f
            // the image is a pin so offset the image so that the pinpoint
            // is on the point rather than the image's true center
            leaderOffsetX = 30f
            offsetY = 25f
        }
        return pinSymbol
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}