package com.stream.video.users.application.plugins

import io.ktor.application.*
import io.ktor.features.*

fun Application.configureCallLogging(){
    install(CallLogging)
}