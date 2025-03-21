package com.bm.arcgis_sample.presentation.map_view.view_model

import android.widget.Toast
import androidx.lifecycle.ViewModel
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repositoryRemoteGeoCode : RemoteDataSourceGeocode,
    private val repositoryLocalSavedAddress : LocalDataSourceSavedAddress,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiStateMapScreen())
    val uiState = _uiState.asStateFlow()
    var addressResponse : DtoReverseGeocodeResponse? = null

    private val _showMessage : MutableSharedFlow<String> = MutableSharedFlow()
    val showMessage = _showMessage.asSharedFlow()

    val mapViewProxy = MapViewProxy()

    val graphicsOverlay = GraphicsOverlay().apply {
        this.isPopupEnabled = true
    }
    val locationDisplay = LocationDisplay().apply {
        showLocation = true
        showPingAnimationSymbol = true
        useCourseSymbolOnMovement = true
        setAutoPanMode(LocationDisplayAutoPanMode.Recenter)
    }

    init {
        observeCurrentLocation()
    }

    private fun observeCurrentLocation(){
        viewModelScope.launch {
            locationDisplay.location.onEach { lc ->
                lc?.let { location->
                    _uiState.update { state ->
                        state.copy(currentLocation = Viewpoint(location.position))
                    }
                }
            }.stateIn(viewModelScope , started = SharingStarted.WhileSubscribed() , null)
        }
    }

    fun navigateToCurrentLocation() {
        viewModelScope.launch {
            locationDisplay.setAutoPanMode(LocationDisplayAutoPanMode.Recenter)
        }
    }

    fun onMapClick(singleTapConfirmedEvent: SingleTapConfirmedEvent) {
        singleTapConfirmedEvent.mapPoint?.let { point ->
            viewModelScope.launch {
                val latLng = GeometryEngine.projectOrNull(point, SpatialReference.wgs84()) as Point
                _uiState.update { it.copy(latLng = latLng) }
                repositoryRemoteGeoCode.fetchAddressFromReverseGeocode(latLng)
                    .collectLatest {
                        when(it){
                            is ScreenState.Error -> {
                                _uiState.update {
                                    it.copy(isLoading = false)
                                }
                            }
                            ScreenState.Loading -> {
                                _uiState.update {
                                    it.copy(isLoading = true)
                                }
                            }
                            is ScreenState.Success<*> -> {
                                addressResponse = it.data as DtoReverseGeocodeResponse
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        title = buildString {
                                            append("Address = ${addressResponse?.address?.LongLabel ?: ""}")
                                            append("\nLat:${latLng.y},\nLng:${latLng.x}")
                                        }
                                    )
                                }
                            }
                        }
                    }

            }
        }
    }

    fun closeCallout() {
       _uiState.update { it.copy(latLng = null) }
    }

    fun saveAddress() {
        addressResponse?.let {
            viewModelScope.launch(Dispatchers.IO) {
                repositoryLocalSavedAddress.saveAddress(it.toDbAddressModel())
                 closeCallout()
                _showMessage.emit("Saved!")
            }
        }
    }
}