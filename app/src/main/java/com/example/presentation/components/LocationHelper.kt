package com.example.presentation.components

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

enum class LocationPermissionStatus { GRANTED, DENIED, PERMANENTLY_DENIED }

private const val FINE = Manifest.permission.ACCESS_FINE_LOCATION
private const val COARSE = Manifest.permission.ACCESS_COARSE_LOCATION

/** True if the user has allowed precise OR approximate location. */
fun hasLocationPermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, FINE) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, COARSE) == PackageManager.PERMISSION_GRANTED

/**
 * Returns a function that shows the system location prompt.
 * [onResult] is called with the outcome after the user answers (or immediately
 * if Android refuses to show the prompt again).
 */
@Composable
fun rememberLocationPermissionRequester(
    onResult: (LocationPermissionStatus) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val granted = results.values.any { it }
        val status = if (granted) {
            LocationPermissionStatus.GRANTED
        } else {
            val activity = context as? Activity
            val canAskAgain = activity != null &&
                    (ActivityCompat.shouldShowRequestPermissionRationale(activity, FINE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(activity, COARSE))
            if (canAskAgain) LocationPermissionStatus.DENIED
            else LocationPermissionStatus.PERMANENTLY_DENIED
        }
        onResult(status)
    }
    return { launcher.launch(arrayOf(FINE, COARSE)) }
}

/** Current latitude/longitude, or null if permission is missing or no location is available. */
@SuppressLint("MissingPermission")
suspend fun getCurrentLatLng(context: Context): Pair<Double, Double>? {
    if (!hasLocationPermission(context)) return null
    val client = LocationServices.getFusedLocationProviderClient(context)
    return suspendCancellableCoroutine { cont ->
        val cts = CancellationTokenSource()
        cont.invokeOnCancellation { cts.cancel() }
        client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cts.token)
            .addOnSuccessListener { loc ->
                cont.resume(loc?.let { it.latitude to it.longitude })
            }
            .addOnFailureListener { cont.resume(null) }
    }
}