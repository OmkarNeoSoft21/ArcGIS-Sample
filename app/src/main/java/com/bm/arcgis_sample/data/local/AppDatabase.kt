package com.bm.arcgis_sample.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bm.arcgis_sample.data.local.dao.DaoSavedAddress
import com.bm.arcgis_sample.data.local.entities.DbModelSavedAddress

@Database(entities = [DbModelSavedAddress::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getDaoSavedAddress(): DaoSavedAddress
}