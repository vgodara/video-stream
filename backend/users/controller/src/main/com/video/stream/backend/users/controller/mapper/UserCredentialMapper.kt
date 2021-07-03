package com.video.stream.backend.users.controller.mapper

import com.video.stream.backend.users.controller.model.UserCredentialModel
import com.video.stream.backend.users.usecase.model.UserCredential

object UserCredentialMapper : Mapper<UserCredentialModel, UserCredential> {
    override fun toModel(data: UserCredential): UserCredentialModel {
        return UserCredentialModel(data.userName, data.password)
    }

    override fun fromModel(model: UserCredentialModel): UserCredential {
        return UserCredential(userName = model.userName, password = model.password)
    }
}