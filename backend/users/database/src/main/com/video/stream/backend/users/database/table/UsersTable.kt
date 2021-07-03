package com.video.stream.backend.users.database.table

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UsersTable : IntIdTable() {
    val userId = varchar("user_id", 50).uniqueIndex()
    val userName = varchar("user_name", 50)
    val passwordHash = varchar("password_hash", 255)
    val salt = varchar("salt", 50)
}

class UserEntry(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntry>(UsersTable)

    var userId by UsersTable.userId
    var userName by UsersTable.userName
    var passwordHash by UsersTable.passwordHash
    var salt by UsersTable.salt

}