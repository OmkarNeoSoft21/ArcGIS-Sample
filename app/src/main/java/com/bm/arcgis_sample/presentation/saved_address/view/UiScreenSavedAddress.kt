package com.bm.arcgis_sample.presentation.saved_address.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bm.arcgis_sample.LocalNavController
import com.bm.arcgis_sample.domain.utill.CustomToolbar
import com.bm.arcgis_sample.presentation.saved_address.component.SavedAddressListItem
import com.bm.arcgis_sample.presentation.saved_address.view_model.SavedAddressViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiScreenSavedAddress(
    viewModel:SavedAddressViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    Scaffold(
        topBar = {
            CustomToolbar("Saved Address" , isBackArrow = true){
                navController.navigateUp()
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            items(uiState.savedAddressList){
                SavedAddressListItem(it)
            }
        }
    }
}