package com.stream.video.users.application.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.user() {
    authenticate {
        get("/user/me") {
            call.respond(HttpStatusCode.OK, "Authentication is valid!")
        }
    }
}