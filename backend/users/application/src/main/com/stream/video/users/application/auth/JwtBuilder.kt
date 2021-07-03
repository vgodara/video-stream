package com.stream.video.users.application.auth

import com.auth0.jwt.interfaces.JWTVerifier

interface JwtBuilder {
    fun makeTokenJwtVerifier(): JWTVerifier
}