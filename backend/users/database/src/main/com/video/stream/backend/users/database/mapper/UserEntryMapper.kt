package com.video.stream.backend.users.database.mapper

import com.video.stream.backend.users.database.table.UserEntry
import com.video.stream.backend.users.datasource.model.UserEntity


object UserEntryMapper : Mapper<UserEntry, UserEntity> {
    override fun UserEntry.toEntity(): UserEntity {
        return UserEntity(
            userId = userId,
            userName = userName,
            passwordHash = passwordHash,
            salt = salt
        )


    }
}