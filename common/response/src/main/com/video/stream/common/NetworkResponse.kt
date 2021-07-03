package com.video.stream.common

import kotlinx.serialization.Serializable

@Serializable
data class NetworkResponse<T>(
    val responseCode: Int,
    val message: String,
    val data: T? = null
)

inline fun <reified T> NetworkResponse<T>.toLocalResponse() =
    data?.let { LocalSuccess(data) } ?: LocalFailure(Throwable(message))