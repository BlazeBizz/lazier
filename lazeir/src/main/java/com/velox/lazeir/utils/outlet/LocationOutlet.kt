package com.velox.lazeir.utils.outlet

import android.content.Context
import com.velox.lazeir.utils.location

fun Context.gpsEnabled() = location.isGpsEnabled(this)

fun Context.openLocationSetting() = location.openLocationSetting(this)

suspend fun Context.getCurrentLocation()  = location.getCurrentLocation(this)