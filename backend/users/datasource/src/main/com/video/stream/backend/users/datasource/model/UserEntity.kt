package com.video.stream.backend.users.datasource.model

data class UserEntity(
    val userId: String,
    val userName: String,
    val passwordHash: String,
    val salt: String,
)