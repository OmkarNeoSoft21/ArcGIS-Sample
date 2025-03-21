package com.bm.arcgis_sample.data.repository.feature_saved_address

import androidx.lifecycle.LiveData
import com.bm.arcgis_sample.data.local.AppDatabase
import com.bm.arcgis_sample.data.local.entities.DbModelSavedAddress
import javax.inject.Inject

class LocalDataSourceSavedAddress @Inject constructor(
    private val appDatabase : AppDatabase
) {

    suspend fun saveAddress(dbModelSavedAddress: DbModelSavedAddress){
        appDatabase.getDaoSavedAddress().insert(dbModelSavedAddress)
    }

    fun getAllAddress():LiveData<List<DbModelSavedAddress>>{
        return appDatabase.getDaoSavedAddress().getSavedAddressList()
    }
}