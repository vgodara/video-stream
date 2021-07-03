package com.stream.video.users.application.plugins


import com.stream.video.users.application.di.KoinModule
import io.ktor.application.*
import org.koin.core.logger.Level

import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger

fun Application.configureKoin() {
    install(Koin) {
        SLF4JLogger(Level.DEBUG)
        modules(KoinModule.applicationModule)
    }
}