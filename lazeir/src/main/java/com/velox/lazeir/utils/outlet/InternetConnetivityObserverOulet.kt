package com.velox.lazeir.utils.outlet

import android.content.Context
import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.MutableLiveData
import com.velox.lazeir.utils.internetObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi


/**
 * [internetConnectivityListener] is a observer which keep observing connectivity of the network
 * and it attaches it self to the lifecycleScope, and uses ConnectivityManager in background
 *
 * Example
 *
 * import androidx.lifecycle.lifecycleScope
 *
 *
internetConnectivityListener(lifecycleScope = lifecycleScope,
context = context,
onAvailable = {},
onLosing = {},
onLost = {},
onUnAvailable = {})

 *
 * it returns  "Available, Unavailable, Losing, Lost" state on connectivity change in the above functions respectively
 * **/
@Keep
@OptIn(ExperimentalCoroutinesApi::class)
fun internetConnectivityListener(
    lifecycleScope: LifecycleCoroutineScope,
    onAvailable: (it: String) -> Unit,
    onUnAvailable: (it: String) -> Unit = {},
    onLosing: (it: String) -> Unit = {},
    onLost: (it: String) -> Unit,
    context: Context
) {
    return internetObserver.internetConnectivityListener(
        lifecycleScope = lifecycleScope,
        onAvailable = onAvailable,
        onUnAvailable = onUnAvailable,
        onLosing = onLosing,
        onLost = onLost,
        context = context
    )
}

/**
* it returns a composable function  "Available, Unavailable, Losing, Lost" state on connectivity change in the above functions respectively
* **/
@Composable
@OptIn(ExperimentalCoroutinesApi::class)
fun InternetConnectivityListener(
    lifecycleScope: LifecycleCoroutineScope,
    stateChangeText: MutableLiveData<String>? = null,
    onAvailable: () -> Unit,
    onUnAvailable: () -> Unit = {},
    onLosing: () -> Unit = {},
    onLost: () -> Unit,
) {
    return internetObserver.InternetConnectivityListener(
        lifecycleScope = lifecycleScope,
        stateChangeText =stateChangeText ,
        onAvailable = onAvailable,
        onUnAvailable = onUnAvailable,
        onLosing = onLosing,
        onLost = onLost
        )
}