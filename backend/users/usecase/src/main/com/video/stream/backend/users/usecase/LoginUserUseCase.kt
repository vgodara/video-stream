package com.video.stream.backend.users.usecase

import com.video.stream.backend.users.usecase.model.User
import com.video.stream.backend.users.usecase.model.UserCredential
import com.video.stream.backend.users.usecase.repository.UserRepository
import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalResponse
import com.video.stream.common.LocalSuccess
import com.video.stream.common.ResponseCode
import com.video.stream.common.users.v1.exception.UserException
import com.video.stream.utilities.checkPasswordAgainstHash
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class LoginUserUseCase(private val repository: UserRepository, executionContext: CoroutineContext) :
    BaseUseCase<UserCredential, User>(executionContext) {
    override fun run(params: UserCredential): Flow<LocalResponse<User>> {
        return repository.findUserByName(params.userName).map { response ->
            when (response) {
                is LocalSuccess -> {
                    val user = response.data
                    val password = params.password
                    val salt = user.salt
                    val passwordHash = user.passwordHash
                    if (checkPasswordAgainstHash(password, salt, passwordHash)) {
                        response
                    } else {
                        LocalFailure(UserException(ResponseCode.FORBIDDEN, "Invalid password"))
                    }
                }
                is LocalFailure -> {
                    response
                }
            }
        }

    }
}