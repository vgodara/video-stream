package com.video.stream.backend.users.database.table

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption


object RefreshTokenTable : IntIdTable("refresh_tokens") {
    val userId = reference("user_id", UsersTable.userId, ReferenceOption.CASCADE)
    val refreshToken = text("refresh_token")
}

class RefreshTokenEntry(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RefreshTokenEntry>(RefreshTokenTable)

    var userId by RefreshTokenTable.userId
    var refreshToken by RefreshTokenTable.refreshToken

}