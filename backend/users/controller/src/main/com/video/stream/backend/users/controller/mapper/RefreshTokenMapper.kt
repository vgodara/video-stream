package com.video.stream.backend.users.controller.mapper

import com.video.stream.backend.users.controller.model.RefreshTokenModel
import com.video.stream.backend.users.usecase.model.RefreshToken

object RefreshTokenMapper : Mapper<RefreshTokenModel, RefreshToken> {
    override fun toModel(data: RefreshToken): RefreshTokenModel {
        return RefreshTokenModel(data.userId, data.refreshToken)
    }

    override fun fromModel(model: RefreshTokenModel): RefreshToken {
        return RefreshToken(model.userId, model.refreshToken)
    }
}