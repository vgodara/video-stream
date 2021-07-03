package com.video.stream.backend.users.usecase

import com.video.stream.backend.users.usecase.model.RefreshToken
import com.video.stream.backend.users.usecase.repository.TokenRepository
import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalResponse
import com.video.stream.common.LocalSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlin.coroutines.CoroutineContext


class RetrieveRefreshTokenUseCase(
    private val repository: TokenRepository,
    private val findUserByIdUseCase: FindUserByIdUseCase,
    executionContext: CoroutineContext,
) : BaseUseCase<RefreshToken, RefreshToken>(executionContext) {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun run(params: RefreshToken): Flow<LocalResponse<RefreshToken>> {
        return findUserByIdUseCase(params.userId).flatMapLatest { response ->
            when (response) {
                is LocalFailure -> {
                    flowOf(LocalFailure(response.throwable))
                }
                is LocalSuccess -> {
                    repository.retrieveRefreshToken(params)
                }
            }
        }
    }
}