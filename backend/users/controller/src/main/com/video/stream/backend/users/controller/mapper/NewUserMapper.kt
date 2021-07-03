package com.video.stream.backend.users.controller.mapper

import com.video.stream.backend.users.controller.model.NewUserModel
import com.video.stream.backend.users.usecase.model.NewUser

object NewUserMapper : Mapper<NewUserModel, NewUser> {
    override fun toModel(data: NewUser): NewUserModel {
        return NewUserModel(data.userName, data.password)
    }

    override fun fromModel(model: NewUserModel): NewUser {
        return NewUser(userName = model.userName, password = model.password)
    }
}