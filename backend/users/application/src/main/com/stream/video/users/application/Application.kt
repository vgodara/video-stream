package com.stream.video.users.application

import com.stream.video.users.application.plugins.*
import com.stream.video.users.application.routes.configureRouting
import io.ktor.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)
fun Application.module(testing: Boolean = false) {
    configureKoin()
    configureDatabase()
    configureSerialization()
    configureSecurity()
    configureCallLogging()
    configureRouting()
    configureHTTP()
}
