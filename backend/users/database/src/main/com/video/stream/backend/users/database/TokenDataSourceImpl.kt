package com.video.stream.backend.users.database

import com.video.stream.backend.users.database.connection.Util.returnFlow
import com.video.stream.backend.users.database.mapper.RefreshTokenEntryMapper.toEntity
import com.video.stream.backend.users.database.table.RefreshTokenEntry
import com.video.stream.backend.users.database.table.RefreshTokenTable
import com.video.stream.backend.users.datasource.model.RefreshTokenEntity
import com.video.stream.backend.users.datasource.model.UserEntity
import com.video.stream.backend.users.datasource.source.TokenDataSource
import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalResponse
import com.video.stream.common.LocalSuccess
import com.video.stream.common.ResponseCode
import com.video.stream.common.users.v1.exception.UserException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

class TokenDataSourceImpl(private val executionContext: CoroutineContext) : TokenDataSource {
    init {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(RefreshTokenTable)
        }

    }

    override fun saveRefreshToken(refreshTokenEntity: RefreshTokenEntity): Flow<LocalResponse<RefreshTokenEntity>> {
        return returnFlow {
            RefreshTokenEntry.new {
                userId = refreshTokenEntity.userId
                refreshToken = refreshTokenEntity.refreshToken
            }
        }.map { response ->
            when (response) {
                is LocalFailure -> {
                    LocalFailure(response.throwable)
                }
                is LocalSuccess -> {
                    LocalSuccess(response.data.toEntity())
                }
            }
        }.flowOn(executionContext)
    }


    override fun retrieveRefreshToken(refreshToken: RefreshTokenEntity): Flow<LocalResponse<RefreshTokenEntity>> {
        return returnFlow {
            RefreshTokenEntry.find {
                (RefreshTokenTable.refreshToken eq refreshToken.refreshToken)
                    .and(
                        RefreshTokenTable.userId eq refreshToken.userId
                    )
            }.firstOrNull()

        }.map { response ->
            when (response) {
                is LocalFailure -> {
                    LocalFailure(response.throwable)
                }
                is LocalSuccess -> {
                    LocalSuccess(response.data.toEntity())
                }
            }
        }.map {
            it.genericToCustomException<NoSuchElementException>(
                ResponseCode.NOT_FOUND,
                "Refresh token with $refreshToken not found"
            )
        }.flowOn(executionContext)
    }

    override fun deleteRefreshToken(refreshTokenEntity: RefreshTokenEntity): Flow<LocalResponse<Unit>> {
        return returnFlow {
            RefreshTokenEntry.find {
                (RefreshTokenTable.userId eq refreshTokenEntity.userId)
                    .and(RefreshTokenTable.refreshToken eq refreshTokenEntity.refreshToken)
            }
                .forEach {
                    it.delete()
                }

        }.map { response ->
            when (response) {
                is LocalFailure -> {
                    LocalFailure(response.throwable)
                }
                is LocalSuccess -> {
                    LocalSuccess(response.data)
                }
            }
        }.flowOn(executionContext)
    }

    private inline fun <reified T : Exception> LocalResponse<RefreshTokenEntity>.genericToCustomException(
        responseCode: Int,
        errorMessage: String,
    ): LocalResponse<RefreshTokenEntity> = when (this) {
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