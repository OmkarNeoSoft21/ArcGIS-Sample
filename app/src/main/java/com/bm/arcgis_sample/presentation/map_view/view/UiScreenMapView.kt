package com.bm.arcgis_sample.presentation.map_view.view

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.GpsFixed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arcgismaps.mapping.ArcGISMap
import com.arcgismaps.mapping.BasemapStyle
import com.arcgismaps.toolkit.geoviewcompose.MapView
import com.arcgismaps.toolkit.geoviewcompose.theme.CalloutDefaults
import com.bm.arcgis_sample.LocalNavController
import com.bm.arcgis_sample.domain.navigation.Route
import com.bm.arcgis_sample.domain.utill.CustomToolbar
import com.bm.arcgis_sample.presentation.map_view.view_model.MapViewModel
import com.meticha.permissions_compose.AppPermission
import com.meticha.permissions_compose.rememberAppPermissionState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiScreenMapView(mapViewModel : MapViewModel = hiltViewModel()) {
    val uiState by mapViewModel.uiState.collectAsStateWithLifecycle()
    val showMessage by mapViewModel.showMessage.collectAsStateWithLifecycle("")
    val permissions = rememberAppPermissionState(permissions = listOf(
            AppPermission(
                permission = Manifest.permission.ACCESS_FINE_LOCATION,
                description = "Required for Fetch your device location. Please grant this permission.",
                isRequired = true
            ),
            AppPermission(
                permission = Manifest.permission.ACCESS_COARSE_LOCATION,
                description = "Required for Fetch your device location. Please grant this permission.",
                isRequired = true
            )
        ))

    val map = remember { createMap() }

    val navController = LocalNavController.current
    val context = LocalContext.current

    LaunchedEffect(showMessage) {
        if (showMessage.isNotEmpty()){
            Toast.makeText(context , showMessage , Toast.LENGTH_SHORT ).show()
        }
    }

    LaunchedEffect(permissions.allRequiredGranted()) {
        mapViewModel.locationDisplay.dataSource.start()
    }
    LaunchedEffect(!permissions.allRequiredGranted()) {
        permissions.requestPermission()
    }

    Scaffold(
        topBar = {
            CustomToolbar("Map View")
        }
    ) {
        Box {
            if (permissions.allRequiredGranted()) {
                MapView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    arcGISMap = map,
                    mapViewProxy = mapViewModel.mapViewProxy,
                    locationDisplay = mapViewModel.locationDisplay,
                    graphicsOverlays = listOf(mapViewModel.graphicsOverlay),
                    onSingleTapConfirmed = { event ->
                        mapViewModel.onMapClick(event)
                    },
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .background(Color.White)
                            .padding(5.dp),
                        textAlign = TextAlign.Center,
                        text = "Tap anywhere to see information and save your favourite address.",
                        style = MaterialTheme.typography.labelSmall
                    )
                    uiState.latLng?.let{ point ->
                        Callout(
                            modifier = Modifier.sizeIn(maxWidth = 250.dp),
                            location = point,
                            shapes = CalloutDefaults.shapes(
                                calloutContentPadding = PaddingValues(4.dp)
                            ),
                            colorScheme = CalloutDefaults.colors(
                                backgroundColor = MaterialTheme.colorScheme.background,
                                borderColor = MaterialTheme.colorScheme.outline
                            )
                        ) {
                            if (uiState.isLoading){
                                LinearProgressIndicator()
                            }else{
                                Icon(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .align(Alignment.TopEnd)
                                        .size(20.dp)
                                        .clickable {
                                            mapViewModel.closeCallout()
                                        },
                                    imageVector = Icons.TwoTone.Close, contentDescription = ""
                                )
                                Column(modifier = Modifier.padding(top = 30.dp)) {
                                    Text(
                                        text = uiState.title,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Button(
                                        modifier = Modifier.align(Alignment.CenterHorizontally).wrapContentSize(),
                                        onClick = {
                                            mapViewModel.saveAddress()

                                        }
                                    ) {
                                        Text(
                                            text = "Save address",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }



            Icon(
                modifier = Modifier
                    .padding(bottom = 50.dp, end = 20.dp)
                    .align(Alignment.BottomEnd)
                    .background(color = Color.White, shape = CircleShape)
                    .size(40.dp)
                    .clickable {
                        mapViewModel.navigateToCurrentLocation()
                    },
                imageVector = Icons.TwoTone.GpsFixed, contentDescription = ""
            )

            Button(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(bottom = 50.dp , start = 20.dp)
                    .align(Alignment.BottomStart),
                onClick = {
                    navController.navigate(Route.SavedAddress)
                }
            ) {
                Text(
                    text = "View Saved Address",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

fun createMap(): ArcGISMap {
    return ArcGISMap(BasemapStyle.ArcGISImagery)
}
