package com.bm.arcgis_sample.presentation.saved_address.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.arcgismaps.geometry.GeometryEngine
import com.arcgismaps.geometry.Point
import com.arcgismaps.geometry.SpatialReference
import com.arcgismaps.location.LocationDisplayAutoPanMode
import com.arcgismaps.mapping.Viewpoint
import com.arcgismaps.mapping.view.GraphicsOverlay
import com.arcgismaps.mapping.view.LocationDisplay
import com.arcgismaps.mapping.view.SingleTapConfirmedEvent
import com.arcgismaps.toolkit.geoviewcompose.MapViewProxy
import com.bm.arcgis_sample.data.repository.feature_geocode.RemoteDataSourceGeocode
import com.bm.arcgis_sample.data.repository.feature_saved_address.LocalDataSourceSavedAddress
import com.bm.arcgis_sample.data.repository.model.DtoReverseGeocodeResponse
import com.bm.arcgis_sample.domain.utill.ScreenState
import com.bm.arcgis_sample.presentation.map_view.state.UiStateMapScreen
import com.bm.arcgis_sample.presentation.saved_address.state.UiStateSavedAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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