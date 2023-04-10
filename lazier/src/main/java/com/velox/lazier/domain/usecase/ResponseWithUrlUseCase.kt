package com.velox.lazier.domain.usecase


import com.velox.lazier.domain.repositories.Repository
import com.velox.lazier.utils.NetworkResource
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import javax.inject.Inject

class ResponseWithUrlUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun invoke(vUrl: String): Flow<NetworkResource<ResponseBody>> {
        return repository.getUrl(vUrl)
    }
}