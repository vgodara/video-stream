package com.video.stream.backend.users.database.mapper

import com.video.stream.backend.users.database.table.RefreshTokenEntry
import com.video.stream.backend.users.datasource.model.RefreshTokenEntity

object RefreshTokenEntryMapper : Mapper<RefreshTokenEntry, RefreshTokenEntity> {
    override fun RefreshTokenEntry.toEntity(): RefreshTokenEntity {
        return RefreshTokenEntity(userId, refreshToken)
    }

}