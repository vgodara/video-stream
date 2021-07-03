package com.video.stream.backend.users.datasource.mapper

import com.video.stream.backend.users.datasource.model.RefreshTokenEntity
import com.video.stream.backend.users.usecase.model.RefreshToken

object RefreshTokenEntityMapper : Mapper<RefreshTokenEntity, RefreshToken> {
    override fun RefreshTokenEntity.fromEntity(): RefreshToken {
        return RefreshToken(userId, refreshToken)
    }

    override fun RefreshToken.toEntity(): RefreshTokenEntity {
        return RefreshTokenEntity(userId, refreshToken)
    }
}