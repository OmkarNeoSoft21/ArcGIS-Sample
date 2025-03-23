package com.bm.arcgis_sample.domain.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object MapScreen : Route

    @Serializable
    data object SavedAddress : Route
}