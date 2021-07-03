package com.stream.video.users.application.routes

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {

    routing {
        signUp()
        login()
        refreshToken()
        user()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
