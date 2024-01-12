package com.velox.lazeir.utils.outlet

import android.annotation.SuppressLint
import com.velox.lazeir.utils.handler
import com.velox.lazeir.utils.NetworkResource
import com.velox.lazeir.utils.handler.handleNetworkResponseInternal
import com.velox.lazeir.utils.handlerV2
import com.velox.lazeir.utils.handler_v2.handleFlowV2
import com.velox.lazeir.utils.handler_v2.handleNetworkCallV2
import com.velox.lazeir.utils.handler_v2.handleNetworkResponseInternalV2
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


fun <T, O> handleNetworkResponse(
    call: suspend () -> Response<T>,/*timeOut: Long = 10000L*/ mapFun: (it: T) -> O
): Flow<NetworkResource<O>> {
    return handler.handleNetworkResponse(call, mapFun/*, timeOut*/)
}

/**
 * [handleNetworkResponse] handle the API response,
 * convert the dto response to domain response
 * extracting the error according to the error code
 *
 * Way to use
 *
 * In Implementation
 *
 * override suspend fun requestData(): Flow<NetworkResource<T>>
 *
 *      {
 *      return apiService.requestData().handleNetworkResponse()
 *      }
 *
 * */
fun <T> Response<T>.handleNetworkResponse(/*timeOut: Long = 10000L*/): Flow<NetworkResource<T>> {
    return handler.handleNetworkResponse(this  /*timeOut*/)
}


/**
 * [handleFlow] takes the response from use case function as Resource<> with in Main Coroutine Scope
 * return the extracted response with in onLoading(),onFailure(),onSuccess()
 * Call within IO Scope
 * **/
 inline fun <T> Flow<NetworkResource<T>>.handleFlow(
    crossinline onLoading: suspend (it: Boolean) -> Unit,
    crossinline onFailure: suspend (it: String, errorObject: JSONObject, code: Int) -> Unit,
    crossinline onSuccess: suspend (it: T) -> Unit
) {
//    return handler.handleFlow(this, onLoading, onFailure, onSuccess)
     return handleFlowV2(this, onLoading, onFailure, onSuccess)
}

 inline fun <T> Flow<NetworkResource<T>>.handleFlowWithScope(
     crossinline onLoading: suspend (it: Boolean) -> Unit,
     crossinline onFailure: suspend (it: String, errorObject: JSONObject, code: Int) -> Unit,
     crossinline onSuccess: suspend (it: T) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
//        return@launch handler.handleFlow(this@handleFlowWithScope, onLoading, onFailure, onSuccess)
        return@launch handleFlowV2(this@handleFlowWithScope, onLoading, onFailure, onSuccess)
    }
}


@SuppressLint("LogNotTimber")
inline fun <reified T> Call<T>.handleNetworkCall(/*timeOut: Long = 10000L*/): Flow<NetworkResource<T>> {
//    return handler.handleNetworkCall(this/*timeOut*/)
    return handleNetworkCallV2(this)
}

/**
 * [handleNetworkResponse] handel the ktor request
 *
 *
 * usage:-
 *
 *
 * fun requestedFeature(request: Request): Flow<NetworkResult<Request>> {
 *
 *         return [handleNetworkResponse] {
 *             httpClient.post(urlString = "https://www.google.com/") {
 *                 setBody(body = request)
 *             }
 *         }
 *     }
 * **/
inline fun <reified T> handleNetworkResponse(crossinline call: suspend () -> HttpResponse): Flow<NetworkResource<T>> =
    handleNetworkResponseInternalV2(call)

