package com.video.stream.backend.users.usecase

import com.video.stream.backend.users.usecase.model.NewUser
import com.video.stream.backend.users.usecase.model.User
import com.video.stream.backend.users.usecase.repository.UserRepository
import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalResponse
import com.video.stream.common.LocalSuccess
import com.video.stream.common.ResponseCode
import com.video.stream.common.users.v1.exception.UserException
import com.video.stream.utilities.generateSalt
import com.video.stream.utilities.generateUUID
import com.video.stream.utilities.passwordEncoder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlin.coroutines.CoroutineContext

class CreateUserUseCase(
    private val repository: UserRepository,
    executionContext: CoroutineContext,
) : BaseUseCase<NewUser, User>(executionContext) {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun run(params: NewUser): Flow<LocalResponse<User>> {
        return repository.findUserByName(params.userName)
            .flatMapLatest {
                when (it) {
                    is LocalSuccess -> {
                        flowOf(
                            LocalFailure(
                                UserException(
                                    ResponseCode.ALREADY_EXIST,
                                    "User with ${params.userName} already exists"
                                )
                            )
                        )
                    }
                    is LocalFailure -> {
                        val exception = it.throwable
                        if ((exception as? UserException)?.responseCode == ResponseCode.NOT_FOUND) {
                            createUser(params)
                        } else flowOf(LocalFailure(exception))

                    }
                }
            }
    }

    private fun createUser(params: NewUser): Flow<LocalResponse<User>> {
        val userId = generateUUID()
        val userName = params.userName
        val password = params.password
        val salt = generateSalt()
        val passwordHash = passwordEncoder(password, salt)
        val userModel = User(
            userId = userId,
            userName = userName,
            passwordHash = passwordHash,
            salt = salt
        )
        return repository.createUser(userModel)
    }
}