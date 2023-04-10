package com.velox.lazier.data.remote

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET
    suspend fun getUrl(@Url url: String): Response<ResponseBody>

    @GET
    suspend fun getWithHeaderAuthorization(
        @Url url: String,
        @Header("Authorization") header: String
    ): Response<ResponseBody>

    @POST
    suspend fun postWithRequestBody(
        @Url url: String,
        @Body body: RequestBody
    ): Response<ResponseBody>

    @POST
    suspend fun postWithHeaderAuthReqBody(
        @Url url: String,
        @Header("Authorization") header: String,
        @Body body: RequestBody
    ): Response<ResponseBody>
}