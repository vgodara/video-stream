package com.video.stream.backend.users.controller

import com.video.stream.backend.users.controller.auth.TokenGenerator
import com.video.stream.backend.users.usecase.CreateUserUseCase
import com.video.stream.backend.users.usecase.SaveRefreshTokenUseCase
import com.video.stream.backend.users.usecase.model.NewUser
import com.video.stream.backend.users.usecase.model.RefreshToken
import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalSuccess
import com.video.stream.common.NetworkResponse
import com.video.stream.common.ResponseCode
import com.video.stream.common.users.v1.request.SignUpRequest
import com.video.stream.common.users.v1.response.LoginResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class RegisterUserController(
    private val createUserUseCase: CreateUserUseCase,
    private val tokenGenerator: TokenGenerator,
    private val saveRefreshToken: SaveRefreshTokenUseCase,
    executionContext: CoroutineContext
) : BaseController<SignUpRequest, LoginResponse>(executionContext) {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun run(params: SignUpRequest): Flow<NetworkResponse<LoginResponse>> {
        return createUserUseCase(NewUser(params.userName, params.password)).flatMapLatest { userResponse ->
            when (userResponse) {
                is LocalFailure -> {
                    val exception = userResponse.throwable
                    flowOf(NetworkResponse(ResponseCode.INTERNAL_SERVER_ERROR, exception.localizedMessage))
                }
                is LocalSuccess -> {
                    val user = userResponse.data
                    val token = tokenGenerator.createToken(user)
                    val refreshToken = tokenGenerator.createRefreshToken(user)
                    saveRefreshToken(RefreshToken(user.userId, refreshToken)).map { refreshTokenResponse ->
                        when (refreshTokenResponse) {
                            is LocalFailure -> {
                                val exception = refreshTokenResponse.throwable
                                NetworkResponse(ResponseCode.INTERNAL_SERVER_ERROR, exception.localizedMessage)
                            }
                            is LocalSuccess -> {
                                val savedRefreshToken = refreshTokenResponse.data
                                if (user.userId == savedRefreshToken.userId && refreshToken == savedRefreshToken.refreshToken)
                                    NetworkResponse(
                                        ResponseCode.OK,
                                        "user created successfully",
                                        LoginResponse(user.userId, user.userName, token, savedRefreshToken.refreshToken)
                                    )
                                else NetworkResponse(
                                    ResponseCode.OK,
                                    "user Created successfully",
                                    LoginResponse(user.userId, user.userName, token)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}