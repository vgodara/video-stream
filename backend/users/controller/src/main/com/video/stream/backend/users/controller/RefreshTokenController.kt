package com.video.stream.backend.users.controller

import com.video.stream.backend.users.controller.auth.TokenGenerator
import com.video.stream.backend.users.controller.auth.TokenPayload
import com.video.stream.backend.users.controller.auth.TokenVerifier
import com.video.stream.backend.users.usecase.FindUserByIdUseCase
import com.video.stream.backend.users.usecase.RetrieveRefreshTokenUseCase
import com.video.stream.backend.users.usecase.model.RefreshToken
import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalSuccess
import com.video.stream.common.NetworkResponse
import com.video.stream.common.ResponseCode
import com.video.stream.common.users.v1.request.RefreshTokenRequest
import com.video.stream.common.users.v1.response.RefreshTokenResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class RefreshTokenController(
    private val retrieveRefreshToken: RetrieveRefreshTokenUseCase,
    private val findUserById: FindUserByIdUseCase,
    private val tokenVerifier: TokenVerifier,
    private val tokenGenerator: TokenGenerator,
    private val tokenPayload: TokenPayload,
    executionContext: CoroutineContext
) : BaseController<RefreshTokenRequest, RefreshTokenResponse>(executionContext) {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun run(params: RefreshTokenRequest): Flow<NetworkResponse<RefreshTokenResponse>> {
        val userId = params.userId ?: throw IllegalStateException("UserId is missing")
        val refreshToken = params.refreshToken

        if (!tokenVerifier.verifyRefreshToken(refreshToken)) {
            return flowOf(NetworkResponse(ResponseCode.FORBIDDEN, "Invalid refresh token"))
        }
        if (userId != tokenPayload.getUserIdFromRefreshToken(refreshToken)) {
            return flowOf(NetworkResponse(ResponseCode.FORBIDDEN, "Invalid refresh token"))
        }
        return findUserById(userId).flatMapLatest { userResponse ->
            when (userResponse) {
                is LocalFailure -> {
                    val exception = userResponse.throwable
                    flowOf(NetworkResponse(ResponseCode.INTERNAL_SERVER_ERROR, exception.localizedMessage))
                }
                is LocalSuccess -> {
                    val user = userResponse.data
                    retrieveRefreshToken(RefreshToken(userId, refreshToken)).map { refreshTokenResponse ->
                        when (refreshTokenResponse) {
                            is LocalFailure -> {
                                val exception = refreshTokenResponse.throwable
                                NetworkResponse(ResponseCode.INTERNAL_SERVER_ERROR, exception.localizedMessage)
                            }
                            is LocalSuccess -> {
                                val savedRefreshToken = refreshTokenResponse.data
                                if (user.userId == userId && refreshToken == savedRefreshToken.refreshToken)
                                    NetworkResponse(
                                        ResponseCode.OK, "New token generated successfully",
                                        RefreshTokenResponse(tokenGenerator.createToken(user))
                                    )
                                else NetworkResponse(ResponseCode.INTERNAL_SERVER_ERROR, "Something went wrong")
                            }

                        }

                    }

                }
            }

        }
    }
}