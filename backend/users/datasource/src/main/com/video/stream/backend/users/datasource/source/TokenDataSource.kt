package com.video.stream.backend.users.datasource.source


import com.video.stream.backend.users.datasource.model.RefreshTokenEntity
import com.video.stream.common.LocalResponse
import kotlinx.coroutines.flow.Flow

interface TokenDataSource {
    fun saveRefreshToken(refreshTokenEntity: RefreshTokenEntity): Flow<LocalResponse<RefreshTokenEntity>>
    fun retrieveRefreshToken(refreshToken: RefreshTokenEntity): Flow<LocalResponse<RefreshTokenEntity>>
    fun deleteRefreshToken(refreshTokenEntity: RefreshTokenEntity): Flow<LocalResponse<Unit>>
}