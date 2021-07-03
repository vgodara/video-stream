package com.stream.video.users.application.routes

import com.video.stream.backend.users.controller.RefreshTokenController
import com.video.stream.common.users.v1.request.RefreshTokenRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import org.koin.ktor.ext.inject

fun Application.refreshToken() {
    val refreshToken: RefreshTokenController by inject()
    routing {
        authenticate {
            post("/user/refreshToken") {
                val userId =
                    call.authentication.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asString()
                        ?: throw IllegalStateException()
                refreshToken(
                    call.receive<RefreshTokenRequest>().copy(userId = userId)
                ).catch { cause ->
                    call.respond(HttpStatusCode.InternalServerError, cause.localizedMessage)
                }.collect { response ->
                    call.respond(HttpStatusCode.OK, response)
                }
            }
        }
    }
}

