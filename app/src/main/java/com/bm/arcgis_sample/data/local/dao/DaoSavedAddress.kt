package com.bm.arcgis_sample.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.bm.arcgis_sample.data.local.entities.DbModelSavedAddress


@Dao
interface DaoSavedAddress {

    @Query("SELECT * FROM SavedAddress")
    fun getSavedAddressList(): LiveData<List<DbModelSavedAddress>>

    @Upsert()
    suspend fun insert(word: DbModelSavedAddress)
}