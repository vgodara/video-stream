package com.video.stream.backend.users.database

import com.video.stream.backend.users.database.connection.Util.returnFlow
import com.video.stream.backend.users.database.mapper.UserEntryMapper.toEntity
import com.video.stream.backend.users.database.table.UserEntry
import com.video.stream.backend.users.database.table.UsersTable
import com.video.stream.backend.users.datasource.model.UserEntity
import com.video.stream.backend.users.datasource.source.UserDataSource
import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalResponse
import com.video.stream.common.LocalSuccess
import com.video.stream.common.ResponseCode
import com.video.stream.common.users.v1.exception.UserException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

class UserDataSourceImpl(private val executionContext: CoroutineContext) : UserDataSource {
    init {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(UsersTable)
        }

    }

    override fun findUserByName(userName: String): Flow<LocalResponse<UserEntity>> {
        return returnFlow {
            UserEntry.find { UsersTable.userName eq userName }.firstOrNull()
        }.map { userResponse ->
            when (userResponse) {
                is LocalFailure -> {
                    LocalFailure(userResponse.throwable)
                }
                is LocalSuccess -> {
                    LocalSuccess(userResponse.data.toEntity())
                }
            }
        }.map {
            it.genericToCustomException<NoSuchElementException>(
                ResponseCode.NOT_FOUND,
                "user with username $userName not found"
            )
        }.flowOn(executionContext)

    }

    override fun findUserById(userId: String): Flow<LocalResponse<UserEntity>> {
        return returnFlow {
            UserEntry.find { UsersTable.userId eq userId }.firstOrNull()
        }.map { userResponse ->
            when (userResponse) {
                is LocalFailure -> {
                    LocalFailure(userResponse.throwable)
                }
                is LocalSuccess -> {
                    LocalSuccess(userResponse.data.toEntity())
                }
            }
        }.map {
            it.genericToCustomException<NoSuchElementException>(
                ResponseCode.NOT_FOUND,
                "User with userId $userId not found"
            )
        }.flowOn(executionContext)
    }

    override fun createUser(userEntity: UserEntity): Flow<LocalResponse<UserEntity>> {
        return returnFlow {
            UserEntry.new {
                userId = userEntity.userId
                userName = userEntity.userName
                passwordHash = userEntity.passwordHash
                salt = userEntity.salt

            }

        }.map { userResponse ->
            when (userResponse) {
                is LocalFailure -> {
                    LocalFailure(userResponse.throwable)
                }
                is LocalSuccess -> {
                    LocalSuccess(userResponse.data.toEntity())
                }
            }
        }.flowOn(executionContext)
    }

    private inline fun <reified T : Exception> LocalResponse<UserEntity>.genericToCustomException(
        responseCode: Int,
        errorMessage: String,
    ): LocalResponse<UserEntity> = when (this) {
        is LocalFailure -> {
            if (throwable is T) {
                LocalFailure(UserException(responseCode, errorMessage))
            } else this
        }
        is LocalSuccess -> {
            this
        }
    }
}
