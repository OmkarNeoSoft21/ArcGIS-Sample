package com.bm.arcgis_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arcgismaps.ApiKey
import com.arcgismaps.ArcGISEnvironment
import com.bm.arcgis_sample.domain.navigation.Route
import com.bm.arcgis_sample.domain.utill.Constants
import com.bm.arcgis_sample.domain.utill.NetworkObserver
import com.bm.arcgis_sample.presentation.map_view.view.UiScreenMapView
import com.bm.arcgis_sample.presentation.saved_address.view.UiScreenSavedAddress
import com.bm.arcgis_sample.ui.theme.DisplayAMap
import dagger.hilt.android.AndroidEntryPoint


val LocalNavController =
    compositionLocalOf<NavHostController> { error("CompositionLocal LocalNavController not present") }


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    lateinit var networkObserver: NetworkObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ArcGISEnvironment.apiKey = ApiKey.create(Constants.API_KEY)
        ArcGISEnvironment.applicationContext = this
        networkObserver = NetworkObserver(this)
        setContent {
            DisplayAMap {
                Surface {
                    val isConnected =
                        networkObserver.isConnectedFlow.collectAsStateWithLifecycle(true)
                    val navController = rememberNavController()
                    if (isConnected.value) {
                        CompositionLocalProvider(LocalNavController provides navController) {
                            NavHost(
                                navController = navController,
                                startDestination = Route.MapScreen
                            ) {
                                composable<Route.MapScreen> {
                                    UiScreenMapView()
                                }

                                composable<Route.SavedAddress> {
                                    UiScreenSavedAddress()
                                }

                            }
                        }
                    } else {
                        NoInternetScreen()
                    }

                }
            }
        }
    }
}

@Composable
fun NoInternetScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon or Image
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "No Internet",
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp),
            tint = Color.Gray
        )

        // Title
        Text(
            text = "No Internet Connection",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Description
        Text(
            text = "Please check your network settings and try again.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

//        // Retry Button
//        Button (
//            onClick = onRetry,
//            modifier = Modifier
//                .padding(top = 20.dp)
//                .fillMaxWidth(0.6f)
//                .height(50.dp),
//            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
//        ) {
//            Text(text = "Retry", fontSize = 16.sp, color = Color.White)
//        }
    }
}

