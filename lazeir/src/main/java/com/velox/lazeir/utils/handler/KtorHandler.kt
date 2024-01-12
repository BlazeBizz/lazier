package com.velox.lazeir.utils.handler

import com.velox.lazeir.utils.NetworkResource
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.apache.http.conn.ConnectTimeoutException
import java.io.IOException
import java.net.SocketTimeoutException

inline fun <reified T> handleNetworkKtorResponseInternal(crossinline call: suspend () -> HttpResponse): Flow<NetworkResource<T>> {
    return flow {
        emit(NetworkResource.Loading(isLoading = true))
        try {
            val response = call.invoke().body<T>()
            emit(NetworkResource.Success(response))
        } catch (e: ClientRequestException) {
            emit(
                NetworkResource.Error(
                    "ClientRequestException",
                    e.response.body(),
                    e.response.status.value
                )
            )

        } catch (e: ServerResponseException) {

            emit(
                NetworkResource.Error(
                    "ServerResponseException",
                    e.response.body(),
                    e.response.status.value
                )
            )

        } catch (e: RedirectResponseException) {

            emit(
                NetworkResource.Error(
                    "RedirectResponseException",
                    e.response.body(),
                    e.response.status.value
                )
            )

        } catch (e: ResponseException) {
            emit(
                NetworkResource.Error(
                    "ResponseException",
                    e.response.body(),
                    e.response.status.value
                )
            )

        } catch (e: ConnectTimeoutException) {

            emit(NetworkResource.Error("Connection Timeout"))
        } catch (e: SocketTimeoutException) {

            emit(NetworkResource.Error("Socket Timeout"))
        } catch (e: IOException) {

            emit(NetworkResource.Error(e.message ?: "Unknown IO Error"))
        } catch (e: Exception) {

            emit(NetworkResource.Error(e.message ?: "Unknown Error"))
        }

        emit(NetworkResource.Loading(isLoading = false))
    }
}