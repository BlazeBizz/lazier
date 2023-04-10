package com.velox.lazier.domain.usecase

import com.velox.lazier.domain.repositories.Repository
import com.velox.lazier.utils.NetworkResource
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

class ResponseWithAuthResBodyUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun invoke(
        url: String,
        auth: String,
        body: RequestBody
    ): Flow<NetworkResource<ResponseBody>> {
        return repository.postWithHeaderAuthReqBody(url, auth, body)
    }
}