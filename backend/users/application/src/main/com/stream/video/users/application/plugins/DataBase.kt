package com.stream.video.users.application.plugins

import com.video.stream.backend.users.database.connection.DbConnection
import io.ktor.application.*
import org.koin.ktor.ext.get

fun Application.configureDatabase() {
    val connection: DbConnection = get()
}