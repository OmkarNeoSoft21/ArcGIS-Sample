package com.bm.arcgis_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arcgismaps.ApiKey
import com.arcgismaps.ArcGISEnvironment
import com.bm.arcgis_sample.domain.navigation.Route
import com.bm.arcgis_sample.domain.utill.Constants
import com.bm.arcgis_sample.presentation.map_view.view.UiScreenMapView
import com.bm.arcgis_sample.presentation.saved_address.view.UiScreenSavedAddress
import com.bm.arcgis_sample.ui.theme.DisplayAMap
import dagger.hilt.android.AndroidEntryPoint


val LocalNavController =
    compositionLocalOf<NavHostController> { error("CompositionLocal LocalNavController not present") }


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ArcGISEnvironment.apiKey = ApiKey.create(Constants.API_KEY)
        ArcGISEnvironment.applicationContext = this

        setContent {
            DisplayAMap {
                Surface {
                    val navController = rememberNavController()
                    CompositionLocalProvider( LocalNavController provides  navController) {
                        NavHost(
                            navController = navController,
                            startDestination = Route.MapScreen
                        ){
                            composable<Route.MapScreen> {
                                UiScreenMapView()
                            }

                            composable<Route.SavedAddress> {
                                UiScreenSavedAddress()
                            }

                        }
                    }
                }
            }
        }
    }
}

