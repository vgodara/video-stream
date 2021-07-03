package com.video.stream.backend.users.usecase.repository

import com.video.stream.backend.users.usecase.model.User
import com.video.stream.common.LocalResponse
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    fun findUserByName(userName: String): Flow<LocalResponse<User>>
    fun findUserById(userId: String): Flow<LocalResponse<User>>
    fun createUser(user: User): Flow<LocalResponse<User>>
}