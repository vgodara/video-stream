package com.video.stream.backend.users.controller.auth

interface TokenPayload {
    fun getUserIdFromToken(token: String): String?
    fun getUserIdFromRefreshToken(refreshToken: String): String?
}