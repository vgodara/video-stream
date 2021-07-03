package com.video.stream.backend.users.datasource

import com.video.stream.backend.users.datasource.Logger.logError
import com.video.stream.backend.users.datasource.mapper.UserEntityMapper.fromEntity
import com.video.stream.backend.users.datasource.mapper.UserEntityMapper.toEntity
import com.video.stream.backend.users.datasource.source.UserDataSource
import com.video.stream.backend.users.usecase.model.User
import com.video.stream.backend.users.usecase.repository.UserRepository
import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalResponse
import com.video.stream.common.LocalSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class UserRepositoryImpl(private val dataSource: UserDataSource, private val executionContext: CoroutineContext) :
    UserRepository {
    override fun findUserByName(userName: String): Flow<LocalResponse<User>> {
        return logError {
            dataSource.findUserByName(userName).map { response ->
                when (response) {
                    is LocalFailure -> {
                        LocalFailure(response.throwable)
                    }
                    is LocalSuccess -> {
                        LocalSuccess(response.data.fromEntity())
                    }

                }
            }.flowOn(executionContext)
        }
    }

    override fun findUserById(userId: String): Flow<LocalResponse<User>> {
        return logError {
            dataSource.findUserById(userId)
                .map { response ->
                    when (response) {
                        is LocalFailure -> {
                            LocalFailure(response.throwable)
                        }
                        is LocalSuccess -> {
                            LocalSuccess(response.data.fromEntity())
                        }

                    }
                }.flowOn(executionContext)
        }
    }

    override fun createUser(user: User): Flow<LocalResponse<User>> {
        return logError {
            dataSource.createUser(user.toEntity())
                .map { response ->
                    when (response) {
                        is LocalFailure -> {
                            LocalFailure(response.throwable)
                        }
                        is LocalSuccess -> {
                            LocalSuccess(response.data.fromEntity())
                        }

                    }
                }.flowOn(executionContext)
        }
    }
}