package com.byteapps.Features.MarkAttendance.data

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Looper
import android.util.Log
import com.byteapps.Features.MarkAttendance.domain.MarkAttendanceRepository
import com.byteapps.geoattendence.Utils.ResultState
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class MarkAttendanceImpl @Inject constructor(): MarkAttendanceRepository {

    @SuppressLint("MissingPermission")
    override suspend fun markAttendance(context: Context): Flow<ResultState<Boolean>> = callbackFlow {
        trySend(ResultState.Loading)

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        var locationCallback:LocationCallback?=null

        try {

            val targetLatitude = 25.1779093
            val targetLongitude = 83.05893660

            val targetLocation = Location("target").apply {
                latitude = targetLatitude
                longitude = targetLongitude
            }

            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
                .setDurationMillis(20000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setWaitForAccurateLocation(true)
                .build()

            locationCallback = object : LocationCallback() {

                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)

                    result.lastLocation?.let { location->

                        Log.d("CURRENT_LOCATION",location.toString())

                        val distance = location.distanceTo(targetLocation)

                        if (distance <= 50) {
                            trySend(ResultState.Success(true))
                            Log.d("CURRENT_LOCATION-1","true")
                            close()
                        } else {
                            trySend(ResultState.Success(false))
                            Log.d("CURRENT_LOCATION-1","false")
                        }
                    }

                }

            }


            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

        } catch (e: SecurityException) {
            trySend(ResultState.Error("Location permission missing."))
            close()
        } catch (e: Exception) {
            trySend(ResultState.Error("Error: ${e.localizedMessage}"))
            close()
        }

        awaitClose {
            if (locationCallback != null) {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }


    @SuppressLint("MissingPermission")
    override suspend fun geoAttendance(context: Context): Flow<ResultState<Boolean>> = callbackFlow {
        trySend(ResultState.Loading)

        val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

        val targetLatitude = 25.1779093
        val targetLongitude = 83.05893660
        val radiusInMeters = 50f

        val geofence = Geofence.Builder()
            .setRequestId("attendance_geofence")
            .setCircularRegion(targetLatitude, targetLongitude, radiusInMeters)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val geofencePendingIntent: PendingIntent by lazy {
            val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        try {
            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                .addOnSuccessListener {
                    Log.d("Geofence", "Geofence added successfully")
                    // Geofence successfully added
                }
                .addOnFailureListener { e ->
                    trySend(ResultState.Error("Error adding geofence: ${e.localizedMessage}"))
                    close()
                }
        } catch (e: SecurityException) {
            trySend(ResultState.Error("Location permission missing."))
            close()
        }

        // Await close is handled here
        awaitClose {
            geofencingClient.removeGeofences(geofencePendingIntent)
        }
    }

    // Receiver to handle geofence transitions
    class GeofenceBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            if (geofencingEvent != null) {
                if (geofencingEvent.hasError()) {
                    Log.e("Geofence", "Error in geofencing event")
                    return
                }
            }

            when (geofencingEvent?.geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    Log.d("Geofence", "Entered geofence")

                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    Log.d("Geofence", "Exited geofence")

                }
                else -> {
                    Log.e("Geofence", "Unknown transition")
                }
            }
        }
    }
}