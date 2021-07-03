package com.video.stream.backend.users.usecase

import com.video.stream.backend.users.usecase.model.User
import com.video.stream.backend.users.usecase.repository.UserRepository
import com.video.stream.common.LocalResponse
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class FindUserByIdUseCase(private val repository: UserRepository, executionContext: CoroutineContext) :
    BaseUseCase<String, User>(executionContext) {
    override fun run(params: String): Flow<LocalResponse<User>> {
        return repository.findUserById(params)
    }
}