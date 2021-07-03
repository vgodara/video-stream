package com.video.stream.backend.users.controller

import com.video.stream.common.NetworkResponse
import com.video.stream.common.ResponseCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

abstract class BaseController<Params, Type>(private val executionContext: CoroutineContext) {
    internal abstract fun run(params: Params): Flow<NetworkResponse<Type>>
    operator fun invoke(params: Params): Flow<NetworkResponse<Type>> {
        return run(params).catch { cause: Throwable ->
            emit(NetworkResponse(ResponseCode.INTERNAL_SERVER_ERROR, cause.localizedMessage))
        }.flowOn(executionContext)
    }
}