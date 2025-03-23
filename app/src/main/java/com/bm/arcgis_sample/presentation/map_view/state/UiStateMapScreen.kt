package com.bm.arcgis_sample.presentation.map_view.state

import com.arcgismaps.geometry.Point
import com.arcgismaps.mapping.Viewpoint

data class UiStateMapScreen(
    val isLoading: Boolean = false,
    val latLng: Point? = null,
    val title: String = "",
    val layerName: String = "",
    val currentLocation: Viewpoint? = null
)
