package com.velox.lazeir.utils

import org.json.JSONObject

/**
 * Author: [cr velox]
 * */
sealed class NetworkResource<T>(
    val data: T? = null,
    val message: String? = null,
    val errorObject: JSONObject? = null,
    val code: Int? = null
) {
    class Success<T>(data: T?) : NetworkResource<T>(data)

    class Error<T>(message: String, errorObject: JSONObject? = JSONObject(), code: Int? = -100) :
        NetworkResource<T>(null, message, errorObject, code)

    class Loading<T>(val isLoading: Boolean) : NetworkResource<T>(null)

}