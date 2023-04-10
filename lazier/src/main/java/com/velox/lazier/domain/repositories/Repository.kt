package com.velox.lazier.domain.repositories

import com.velox.lazier.utils.NetworkResource
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import okhttp3.ResponseBody

interface Repository {
    suspend fun getUrl(vUrl: String): Flow<NetworkResource<ResponseBody>>
    suspend fun getWithHeaderAuth(url: String, header: String): Flow<NetworkResource<ResponseBody>>
    suspend fun postWithReqBody(url: String, body: RequestBody): Flow<NetworkResource<ResponseBody>>

    suspend fun postWithHeaderAuthReqBody(
        url: String,
        header: String,
        body: RequestBody
    ): Flow<NetworkResource<ResponseBody>>
}