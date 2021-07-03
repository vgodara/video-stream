package com.video.stream.backend.users.datasource.mapper

import com.video.stream.backend.users.datasource.model.UserEntity
import com.video.stream.backend.users.usecase.model.User

object UserEntityMapper : Mapper<UserEntity, User> {
    override fun UserEntity.fromEntity(): User {
        return User(
            userId = userId,
            userName = userName,
            passwordHash = passwordHash,
            salt = salt
        )

    }

    override fun User.toEntity(): UserEntity {
        return UserEntity(userId = userId, userName = userName, passwordHash = passwordHash, salt = salt)
    }
}
