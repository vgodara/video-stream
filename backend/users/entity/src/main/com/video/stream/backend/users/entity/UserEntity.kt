package com.video.stream.backend.users.entity

sealed class UserEntity(open val userId: String)


data class GuestUsers(
    override val userId: String,
) : UserEntity(userId)

data class RegisteredUser(
    override val userId: String,
    val userName: String,
    val passWordHash: String,
    val salt: String,
) : UserEntity(userId)