package com.velox.lazeir.utils.handler_v2


import android.annotation.SuppressLint

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Callback

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

import android.telecom.Call
import com.google.gson.JsonSyntaxException
import com.velox.lazeir.utils.NetworkResource
import com.velox.lazeir.utils.handler.awaitHandler
import com.velox.lazeir.utils.handler.getJSONObject
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException


inline fun <T, O> handleNetworkResponseV2(
    crossinline call: suspend () -> Response<T>, crossinline mapFun: (it: T) -> O
): Flow<NetworkResource<O>> {
    return flow {
        emit(NetworkResource.Loading(true))
        try {

            val response = call.invoke()
            if (response.isSuccessful) {
                val data = response.body()?.let { mapFun(it) }
                emit(NetworkResource.Success(data))
            } else {
                val code = response.code()
                val errorBody = response.errorBody()!!.string()
                try {
                    val jObjError = JSONObject(errorBody)
                    emit(NetworkResource.Error("Response Error", jObjError, code))
                } catch (e: Exception) {
                    e.message?.let { emit(NetworkResource.Error(it, null, code)) }
                }
            }
        }
//        catch (e: HttpException) {
//            val errorBody = e.response()?.getJSONObject()
//            val code = e.code()
//            val message = e.message()
//            e.message?.let { emit(NetworkResource.Error(message, errorBody, code)) }
//        }
        catch (e: TimeoutException) {
            e.message?.let { emit(NetworkResource.Error("Time Out")) }
        } catch (e: SocketTimeoutException) {
            e.message?.let { emit(NetworkResource.Error("Time Out")) }
        } catch (e: IOException) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        } catch (e: IllegalStateException) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        } catch (e: NullPointerException) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        } catch (e: JsonSyntaxException) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        } catch (e: Exception) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        }
        emit(NetworkResource.Loading(false))

    }.flowOn(Dispatchers.IO)
}


inline fun <reified T> handleNetworkResponseV2(response: Response<T>): Flow<NetworkResource<T>> {
    return flow {
        emit(NetworkResource.Loading(isLoading = true))
        try {
            if (response.isSuccessful) {
                emit(NetworkResource.Success(response.body()))
            } else {
                val code = response.code()
                val errorBody = response.errorBody()?.string()
                try {
                    val jObjError = errorBody?.let { JSONObject(it) }
                    emit(NetworkResource.Error("Network Error", jObjError, code))
                } catch (e: Exception) {
                    emit(NetworkResource.Error("UNKNOWN ERROR", code = code))
                }
            }
        } catch (e: TimeoutException) {
            e.message?.let { emit(NetworkResource.Error("Time Out")) }
        } catch (e: SocketTimeoutException) {
            e.message?.let { emit(NetworkResource.Error("Time Out")) }
        } catch (e: IOException) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        }  catch (e: IllegalStateException) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        } catch (e: NullPointerException) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        } catch (e: JsonSyntaxException) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        } catch (e: Exception) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        } finally {
            emit(NetworkResource.Loading(isLoading = false))
        }
        emit(NetworkResource.Loading(isLoading = false))
    }.flowOn(Dispatchers.IO)
}


/**
 * [handleFlow] takes the response from use case function as Resource<> with in Main Coroutine Scope
 * return the extracted response with in onLoading(),onFailure(),onSuccess()
 * Call within IO Scope
 * **/
inline fun <T> handleFlowV2(
    flow: Flow<NetworkResource<T>>,
    crossinline onLoading: suspend (it: Boolean) -> Unit,
    crossinline onFailure: suspend (it: String, errorObject: JSONObject, code: Int) -> Unit,
    crossinline onSuccess: suspend (it: T) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        flow.collectLatest {
            when (it) {
                is NetworkResource.Error -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        onFailure.invoke(it.message!!, it.errorObject!!, it.code!!)
                    }
                }

                is NetworkResource.Loading -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        onLoading.invoke(it.isLoading)
                    }
                }

                is NetworkResource.Success -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        onSuccess.invoke(it.data!!)
                    }
                }
            }
        }
    }
}


@SuppressLint("LogNotTimber")
inline fun < reified T> handleNetworkCallV2(call: retrofit2.Call<T>): Flow<NetworkResource<T>> {
    var code: Int?
    return flow {
        emit(NetworkResource.Loading(isLoading = true))
        try {
            val apiCall = call.awaitHandler()
            if (apiCall.isSuccessful) {
                code = apiCall.code()
                val body = apiCall.body()
                emit(NetworkResource.Success(body))
            } else {
                val errorBody = apiCall.getJSONObject()
                code = apiCall.code()
                val message = apiCall.message()
                emit(NetworkResource.Error(message, errorBody, code))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.getJSONObject()
            code = e.code()
            val message = e.message()
            e.message?.let { emit(NetworkResource.Error(message, errorBody, code)) }
        } catch (e: TimeoutException) {
            e.message?.let { emit(NetworkResource.Error("Time Out")) }
        } catch (e: SocketTimeoutException) {
            e.message?.let { emit(NetworkResource.Error("Time Out")) }
        } catch (e: IOException) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        } catch (e: Exception) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        } catch (e: JsonSyntaxException) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        }
        emit(NetworkResource.Loading(isLoading = false))

    }.flowOn(Dispatchers.IO)
}


inline fun <reified T> handleNetworkResponseInternalV2(crossinline call: suspend () -> HttpResponse): Flow<NetworkResource<T>> {
    return flow {
        emit(NetworkResource.Loading(isLoading = true))
        try {
            val response = call.invoke().body<T>()
            emit(NetworkResource.Success(response))
        } catch (e: ClientRequestException) {
            emit(NetworkResource.Error("ClientRequestException",e.response.body(),e.response.status.value ))

        } catch (e: ServerResponseException) {

            emit(NetworkResource.Error("ServerResponseException",e.response.body(),e.response.status.value ))

        } catch (e: RedirectResponseException) {

            emit(NetworkResource.Error("RedirectResponseException",e.response.body(),e.response.status.value ))

        }catch (e: ResponseException) {
            emit(NetworkResource.Error("ResponseException",e.response.body(),e.response.status.value ))

        } catch (e: ConnectTimeoutException) {

            emit(NetworkResource.Error("Connection Timeout"))
        } catch (e: SocketTimeoutException) {

            emit(NetworkResource.Error("Socket Timeout"))
        } catch (e: IOException) {

            emit(NetworkResource.Error(e.message ?: "Unknown IO Error"))
        } catch (e: TimeoutException) {
            emit(NetworkResource.Error(e.message ?: "Unknown IO Error"))
        } catch (e: Exception) {

            emit(NetworkResource.Error(e.message ?: "Unknown Error"))
        }

        emit(NetworkResource.Loading(isLoading = false))
    }
}

//cr velox