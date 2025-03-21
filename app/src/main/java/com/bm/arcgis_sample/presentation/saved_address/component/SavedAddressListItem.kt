package com.bm.arcgis_sample.presentation.saved_address.component

import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.bm.arcgis_sample.data.local.entities.DbModelSavedAddress

@Composable
fun SavedAddressListItem(model:DbModelSavedAddress){
    ListItem(
        headlineContent = {
            Text(model.addressLine)
        },
        supportingContent = {
            Text("Lat : ${model.lat}\nLong : ${model.long} " )
        }
    )
}