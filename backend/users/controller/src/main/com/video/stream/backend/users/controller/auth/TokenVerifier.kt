package com.video.stream.backend.users.controller.auth

interface TokenVerifier {
    fun verifyToken(token: String): Boolean
    fun verifyRefreshToken(refreshToken: String): Boolean
}