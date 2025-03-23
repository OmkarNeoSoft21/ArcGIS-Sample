package com.bm.arcgis_sample.presentation.saved_address.state

import com.bm.arcgis_sample.data.local.entities.DbModelSavedAddress

data class UiStateSavedAddress(
    val savedAddressList: List<DbModelSavedAddress>
)