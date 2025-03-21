package com.bm.arcgis_sample.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("SavedAddress")
data class DbModelSavedAddress(
    @PrimaryKey(autoGenerate = true)val id:Int = 0,
    val addressLine: String,
    val pinCode: String,
    val lat: String,
    val long: String
)
