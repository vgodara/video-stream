package com.video.stream.backend.users.usecase


import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

abstract class BaseUseCase<Params, Type>(private val executionContext: CoroutineContext) {
    internal abstract fun run(params: Params): Flow<LocalResponse<Type>>
    operator fun invoke(params: Params): Flow<LocalResponse<Type>> {
        return run(params).catch { cause: Throwable ->
            emit(LocalFailure(cause))
        }.flowOn(executionContext)
    }
}