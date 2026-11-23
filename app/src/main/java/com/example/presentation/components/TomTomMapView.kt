package com.example.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tomtom.sdk.init.TomTomSdk
import com.tomtom.sdk.location.GeoPoint
import com.tomtom.sdk.map.display.MapLocationInfrastructure
import com.tomtom.sdk.map.display.camera.InitialCameraOptions
import com.tomtom.sdk.map.display.compose.TomTomMap
import com.tomtom.sdk.map.display.compose.model.MapDisplayInfrastructure
import com.tomtom.sdk.map.display.compose.state.rememberMapViewState
import kotlinx.coroutines.delay

/** TomTom map centred on the given position. Waits until the TomTom SDK has finished starting. */
@Composable
fun TomTomMapView(
    centerLat: Double,
    centerLng: Double,
    modifier: Modifier = Modifier
) {
    var sdkReady by remember { mutableStateOf(TomTomSdk.isInitialized) }
    LaunchedEffect(Unit) {
        while (!TomTomSdk.isInitialized) delay(200)
        sdkReady = true
    }

    if (sdkReady) {
        // Rebuild the map when the centre changes, so the camera moves to the new position
        key(centerLat, centerLng) {
            TomTomMapContent(centerLat, centerLng, modifier)
        }
    } else {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun TomTomMapContent(lat: Double, lng: Double, modifier: Modifier) {
    val initialCameraOptions: InitialCameraOptions = InitialCameraOptions.LocationBased(
        position = GeoPoint(latitude = lat, longitude = lng),
        zoom = 14.0,
    )
    val mapDisplayInfrastructure = MapDisplayInfrastructure(
        sdkContext = TomTomSdk.sdkContext,
    ) {
        locationInfrastructure = MapLocationInfrastructure {
            locationProvider = TomTomSdk.locationProvider
        }
    }
    val mapViewState = rememberMapViewState(initialCameraOptions = initialCameraOptions)

    TomTomMap(
        state = mapViewState,
        infrastructure = mapDisplayInfrastructure,
        modifier = modifier,
    )
}