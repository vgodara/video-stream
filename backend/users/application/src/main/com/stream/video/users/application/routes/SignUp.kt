package com.stream.video.users.application.routes

import com.video.stream.backend.users.controller.RegisterUserController
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import org.koin.ktor.ext.inject

fun Route.signUp() {
    val registerUser: RegisterUserController by inject()
    post("/user/signup") {
        registerUser(call.receive()).catch { cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.localizedMessage)
        }.collect { response ->
            call.respond(HttpStatusCode.OK, response)
        }
    }
}