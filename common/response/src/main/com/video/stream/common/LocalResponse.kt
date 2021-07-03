package com.video.stream.common

sealed class LocalResponse<T>

data class LocalSuccess<T>(val data: T) : LocalResponse<T>()

data class LocalFailure<T>(val throwable: Throwable) : LocalResponse<T>()
