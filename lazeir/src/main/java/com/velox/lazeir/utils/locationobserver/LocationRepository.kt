package com.velox.lazeir.utils.locationobserver

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.annotation.Keep
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.velox.lazeir.utils.hasLocationPermission
import kotlinx.coroutines.tasks.await


class LocationRepository : LocationRepositoryInterface {


    override fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun openLocationSetting(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(context: Context): CurrentLocationData? {

        val priority = Priority.PRIORITY_HIGH_ACCURACY
        if (context.hasLocationPermission()) {
            if (isGpsEnabled(context)) {
                val locationClient = LocationServices.getFusedLocationProviderClient(context)
                val result =
                    locationClient.getCurrentLocation(priority, CancellationTokenSource().token)
                        .await()
                return result?.let { fetchedLocation ->
                    return@let CurrentLocationData(
                        lat = fetchedLocation.latitude.toString(),
                        long = fetchedLocation.longitude.toString(),
                        alt = fetchedLocation.altitude.toString(),
                        bearing = fetchedLocation.bearing.toString(),
                        accuracy = fetchedLocation.accuracy,
                        verticalAccuracy = fetchedLocation.verticalAccuracyMeters
                    )
                }
            } else {
                return null
            }
        } else {
            return null
        }

    }
}

@Keep
data class CurrentLocationData(
    val lat: String,
    val long: String,
    val alt: String? = null,
    val bearing: String? = null,
    val accuracy: Float? = null,
    val verticalAccuracy: Float? = null,
)