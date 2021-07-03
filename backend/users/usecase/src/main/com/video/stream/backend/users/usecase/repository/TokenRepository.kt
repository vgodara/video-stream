package com.video.stream.backend.users.usecase.repository

import com.video.stream.backend.users.usecase.model.RefreshToken
import com.video.stream.common.LocalResponse
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun saveRefreshToken(refreshToken: RefreshToken): Flow<LocalResponse<RefreshToken>>
    fun retrieveRefreshToken(refreshToken: RefreshToken): Flow<LocalResponse<RefreshToken>>
    fun deleteRefreshToken(refreshToken: RefreshToken): Flow<LocalResponse<Unit>>
}