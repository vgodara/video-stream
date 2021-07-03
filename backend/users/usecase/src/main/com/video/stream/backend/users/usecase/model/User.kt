package com.video.stream.backend.users.usecase.model

data class User(val userId: String, val userName: String, val passwordHash: String, val salt: String)
