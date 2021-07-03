package com.stream.video.users.application.routes

import com.video.stream.backend.users.controller.LoginUserController
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import org.koin.ktor.ext.inject

fun Route.login() {
    val loginUser: LoginUserController by inject()
    post("/user/login") {
        loginUser(call.receive()).catch { cause: Throwable ->
            print(cause.stackTrace)
            call.respond(HttpStatusCode.InternalServerError, cause.localizedMessage)
        }.collect { response ->
            call.respond(HttpStatusCode.OK, response)
        }

    }
}


