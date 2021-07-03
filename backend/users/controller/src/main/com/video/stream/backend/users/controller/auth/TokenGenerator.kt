package com.video.stream.backend.users.controller.auth

import com.video.stream.backend.users.usecase.model.User

interface TokenGenerator {
    fun createToken(user: User): String
    fun createRefreshToken(user: User): String
}