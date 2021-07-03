package com.video.stream.backend.users.controller

import com.video.stream.backend.users.usecase.FindUserByIdUseCase
import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalSuccess
import com.video.stream.common.NetworkResponse
import com.video.stream.common.ResponseCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class FindUserByIdController(private val findUserByIdUseCase: FindUserByIdUseCase, executionContext: CoroutineContext) :
    BaseController<String, Boolean>(executionContext) {
    override fun run(params: String): Flow<NetworkResponse<Boolean>> {
        return findUserByIdUseCase(params).map { localResponse ->
            when (localResponse) {
                is LocalFailure -> NetworkResponse(
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    localResponse.throwable.localizedMessage
                )
                is LocalSuccess -> {
                    val result = localResponse.data.userId == params
                    val message = "User with userId $params ${if (result) "not " else ""}found"
                    NetworkResponse(ResponseCode.OK, message, result)
                }
            }
        }
    }
}