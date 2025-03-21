package com.bm.arcgis_sample.data.repository.feature_geocode

import com.arcgismaps.geometry.Point
import com.bm.arcgis_sample.data.remote.ApiService
import com.bm.arcgis_sample.data.remote.Webservice
import com.bm.arcgis_sample.domain.utill.ScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteDataSourceGeocode(
    private val apiService: ApiService
) {
    fun fetchAddressFromReverseGeocode(point:Point): Flow<ScreenState> {
       return flow {
            emit(ScreenState.Loading)
            apiService
                .getAddressFromReverseGeoCode(Webservice.getReverseCodeApiUrl(point))
                .let {
                    if (it.isSuccessful) {
                        emit(ScreenState.Success(it.body()))
                    }else{
                        emit(ScreenState.Error(it.message()))
                    }
                }
        }
    }
}