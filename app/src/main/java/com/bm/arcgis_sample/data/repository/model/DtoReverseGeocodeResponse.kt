package com.bm.arcgis_sample.data.repository.model

import com.bm.arcgis_sample.data.local.entities.DbModelSavedAddress

data class DtoReverseGeocodeResponse(
    val address: Address? = null,
    val location: Location? = null
){
    fun toDbAddressModel():DbModelSavedAddress{
       return DbModelSavedAddress(
           addressLine = address?.LongLabel ?: "",
           pinCode = address?.Postal ?: "",
           lat = location?.y.toString(),
           long = location?.x.toString()
       )
    }
}