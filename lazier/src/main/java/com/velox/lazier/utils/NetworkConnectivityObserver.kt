package com.velox.lazier.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
fun internetConnectivityListener(
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context,
    stateChangeText: MutableLiveData<String>? = null,
    onAvailable: () -> Unit,
    onUnAvailable: () -> Unit = {},
    onLosing: () -> Unit = {},
    onLost: () -> Unit,
) {
    val connectivityObserver = NetworkConnectivityObserver(applicationContext)
    connectivityObserver.observe().onEach {
        stateChangeText?.value = it.name
        when (it) {
            ConnectivityObserver.Status.Available -> onAvailable()
            ConnectivityObserver.Status.Unavailable -> onUnAvailable()
            ConnectivityObserver.Status.Losing -> onLosing()
            ConnectivityObserver.Status.Lost -> onLost()
        }
    }.launchIn(lifecycleScope)
}

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
    val applicationContext = LocalContext.current
    val connectivityObserver = NetworkConnectivityObserver(applicationContext)
    LaunchedEffect(connectivityObserver){
        connectivityObserver.observe().onEach {
            stateChangeText?.value = it.name
            when (it) {
                ConnectivityObserver.Status.Available -> onAvailable()
                ConnectivityObserver.Status.Unavailable -> onUnAvailable()
                ConnectivityObserver.Status.Losing -> onLosing()
                ConnectivityObserver.Status.Lost -> onLost()
            }
        }.launchIn(lifecycleScope)

    }
}


@ExperimentalCoroutinesApi
private class NetworkConnectivityObserver(
    context: Context
) : ConnectivityObserver {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        send(ConnectivityObserver.Status.Available)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch {
                        send(ConnectivityObserver.Status.Losing)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        send(ConnectivityObserver.Status.Lost)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        send(ConnectivityObserver.Status.Unavailable)
                    }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }
}


private interface ConnectivityObserver {
    fun observe(): Flow<Status>
    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}