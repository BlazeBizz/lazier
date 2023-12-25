package com.velox.lazeir.utils.handler

import android.annotation.SuppressLint
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

interface HandlerInterface {
    fun <T, O> handleNetworkResponse(
        call: suspend () -> Response<T>, mapFun: (it: T) -> O/*, timeOut: Long*/
    ): Flow<NetworkResource<O>>


    fun <T> handleNetworkResponse(
        response: Response<T>/*, timeOut: Long*/
    ): Flow<NetworkResource<T>>


    suspend fun <T> handleFlow(
        flow: Flow<NetworkResource<T>>,
        onLoading: suspend (it: Boolean) -> Unit,
        onFailure: suspend (it: String, errorObject: JSONObject, code: Int) -> Unit,
        onSuccess: suspend (it: T) -> Unit
    )


    @SuppressLint("LogNotTimber")
    fun <T> handleNetworkCall(
        call: Call<T>/*, timeOut: Long*/
    ): Flow<NetworkResource<T>>

}