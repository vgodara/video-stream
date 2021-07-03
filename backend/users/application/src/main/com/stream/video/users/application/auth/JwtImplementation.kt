package com.stream.video.users.application.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.video.stream.backend.users.usecase.model.User
import java.util.*

class JwtImplementation(
    private val issuer: String,
    private val audience: String,
    private val tokenSecret: String,
    private val refreshTokenSecret: String
) : AuthenticationHelper {
    private val validityInMs: Long = 3600000L * 24L

    private val refersValidityInMs: Long = 3600000L * 24L * 30L

    private val tokenAlgorithm by lazy {
        Algorithm.HMAC512(tokenSecret)
    }
    private val refreshTokenAlgorithm by lazy {
        Algorithm.HMAC512(refreshTokenSecret)
    }
    private val tokenJwtVerifier by lazy {
        JWT.require(tokenAlgorithm)
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }

    private val refreshTokenJwtVerifier by lazy {
        JWT.require(refreshTokenAlgorithm)
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }

    override fun makeTokenJwtVerifier(): JWTVerifier {
        return tokenJwtVerifier

    }

    override fun createToken(user: User): String {
        return JWT.create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", user.userId)
            .withClaim("name", user.userName)
            .withExpiresAt(getTokenExpiration(validityInMs))
            .sign(tokenAlgorithm)
    }


    override fun createRefreshToken(user: User): String {
        return JWT.create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", user.userId)
            .withClaim("name", user.userName)
            .withExpiresAt(getTokenExpiration(refersValidityInMs))
            .sign(refreshTokenAlgorithm)
    }

    override fun verifyToken(token: String): Boolean {
        return try {
            tokenJwtVerifier.verify(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun verifyRefreshToken(refreshToken: String): Boolean {
        return try {
            refreshTokenJwtVerifier.verify(refreshToken)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getUserIdFromToken(token: String): String? {
        return try {
            tokenJwtVerifier.verify(token).getClaim("userId").asString()
        } catch (e: Exception) {
            null
        }
    }

    override fun getUserIdFromRefreshToken(refreshToken: String): String? {
        return try {
            refreshTokenJwtVerifier.verify(refreshToken).getClaim("userId").asString()
        } catch (e: Exception) {
            null
        }
    }

    private fun getTokenExpiration(validity: Long) = Date(System.currentTimeMillis() + validity)

}