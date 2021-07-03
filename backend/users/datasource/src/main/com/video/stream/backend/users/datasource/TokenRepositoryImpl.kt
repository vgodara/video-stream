package com.video.stream.backend.users.datasource

import com.video.stream.backend.users.datasource.Logger.logError
import com.video.stream.backend.users.datasource.mapper.RefreshTokenEntityMapper.fromEntity
import com.video.stream.backend.users.datasource.mapper.RefreshTokenEntityMapper.toEntity
import com.video.stream.backend.users.datasource.source.TokenDataSource
import com.video.stream.backend.users.usecase.model.RefreshToken
import com.video.stream.backend.users.usecase.repository.TokenRepository
import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalResponse
import com.video.stream.common.LocalSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class TokenRepositoryImpl(private val dataSource: TokenDataSource, private val executionContext: CoroutineContext) :
    TokenRepository {


    override fun saveRefreshToken(refreshToken: RefreshToken): Flow<LocalResponse<RefreshToken>> {
        return logError {
            dataSource.saveRefreshToken(refreshToken.toEntity())
                .map { response ->
                    when (response) {
                        is LocalFailure -> {
                            LocalFailure(response.throwable)
                        }
                        is LocalSuccess -> {
                            val refreshTokenEntity = response.data
                            LocalSuccess(refreshTokenEntity.fromEntity())
                        }
                    }
                }.flowOn(executionContext)
        }
    }


    override fun retrieveRefreshToken(refreshToken: RefreshToken): Flow<LocalResponse<RefreshToken>> {
        return logError {
            dataSource.retrieveRefreshToken(refreshToken.toEntity())
                .map { response ->
                    when (response) {
                        is LocalFailure -> {
                            LocalFailure(response.throwable)
                        }
                        is LocalSuccess -> {
                            val refreshTokenEntity = response.data
                            LocalSuccess(refreshTokenEntity.fromEntity())
                        }
                    }
                }.flowOn(executionContext)
        }
    }

    override fun deleteRefreshToken(refreshToken: RefreshToken): Flow<LocalResponse<Unit>> {
        return logError { dataSource.deleteRefreshToken(refreshToken.toEntity()) }.flowOn(executionContext)
    }

}