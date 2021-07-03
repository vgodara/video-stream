package com.video.stream.backend.users.datasource.source

import com.video.stream.backend.users.datasource.model.UserEntity
import com.video.stream.common.LocalResponse
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun findUserByName(userName: String): Flow<LocalResponse<UserEntity>>
    fun findUserById(userId: String): Flow<LocalResponse<UserEntity>>
    fun createUser(userEntity: UserEntity): Flow<LocalResponse<UserEntity>>
}