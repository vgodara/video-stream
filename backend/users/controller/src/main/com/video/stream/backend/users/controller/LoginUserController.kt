package com.video.stream.backend.users.controller

import com.video.stream.backend.users.controller.auth.TokenGenerator
import com.video.stream.backend.users.usecase.LoginUserUseCase
import com.video.stream.backend.users.usecase.SaveRefreshTokenUseCase
import com.video.stream.backend.users.usecase.model.RefreshToken
import com.video.stream.backend.users.usecase.model.UserCredential
import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalSuccess
import com.video.stream.common.NetworkResponse
import com.video.stream.common.ResponseCode
import com.video.stream.common.users.v1.request.LoginRequest
import com.video.stream.common.users.v1.response.LoginResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class LoginUserController(
    private val loginUserUseCase: LoginUserUseCase,
    private val saveRefreshToken: SaveRefreshTokenUseCase,
    private val tokenGenerator: TokenGenerator,
    executionContext: CoroutineContext
) :
    BaseController<LoginRequest, LoginResponse>(executionContext) {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun run(params: LoginRequest): Flow<NetworkResponse<LoginResponse>> {
        return loginUserUseCase(UserCredential(params.userName, params.password)).flatMapLatest { localResponse ->
            when (localResponse) {
                is LocalFailure -> {
                    flowOf(NetworkResponse(ResponseCode.INTERNAL_SERVER_ERROR, localResponse.throwable.localizedMessage))
                }
                is LocalSuccess -> {
                    val user = localResponse.data
                    val token = tokenGenerator.createToken(user)
                    val refreshToken = tokenGenerator.createRefreshToken(user)
                    saveRefreshToken(RefreshToken(user.userId, refreshToken)).map { refreshTokenResponse ->
                        when (refreshTokenResponse) {
                            is LocalFailure -> {
                                NetworkResponse(
                                    ResponseCode.INTERNAL_SERVER_ERROR,
                                    refreshTokenResponse.throwable.localizedMessage
                                )
                            }
                            is LocalSuccess -> {
                                val refreshTokenResult = refreshTokenResponse.data
                                if (refreshTokenResult.userId == user.userId) {
                                    if (refreshTokenResult.refreshToken == refreshToken) {
                                        NetworkResponse(
                                            ResponseCode.OK,
                                            "User login successful",
                                            LoginResponse(user.userId, user.userName, token, refreshToken)
                                        )
                                    } else {
                                        NetworkResponse(
                                            ResponseCode.OK,
                                            "User login successful",
                                            LoginResponse(user.userId, user.userName, token)
                                        )
                                    }
                                } else {
                                    NetworkResponse(ResponseCode.INTERNAL_SERVER_ERROR, "Something went wrong")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}