package com.bm.arcgis_sample.presentation.saved_address.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.bm.arcgis_sample.data.repository.feature_saved_address.LocalDataSourceSavedAddress
import com.bm.arcgis_sample.presentation.saved_address.state.UiStateSavedAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedAddressViewModel @Inject constructor(
    private val repositoryLocalSavedAddress : LocalDataSourceSavedAddress,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiStateSavedAddress(arrayListOf()))
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            repositoryLocalSavedAddress
                .getAllAddress()
                .asFlow()
                .collectLatest { list ->
                _uiState.update { it.copy(savedAddressList = list) }
            }
        }
    }
}