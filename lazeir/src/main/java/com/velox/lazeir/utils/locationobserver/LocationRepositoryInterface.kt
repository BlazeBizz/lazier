package com.velox.lazeir.utils.locationobserver

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.Flow

interface LocationRepositoryInterface {

    fun isGpsEnabled(context: Context): Boolean
    fun openLocationSetting(context: Context)
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): CurrentLocationData?
}