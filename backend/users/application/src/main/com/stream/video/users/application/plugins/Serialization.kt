package com.stream.video.users.application.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json


fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json{
            ignoreUnknownKeys=true
            encodeDefaults = true
            isLenient = true
            allowSpecialFloatingPointValues = true
            allowStructuredMapKeys = true
            prettyPrint = false
            useArrayPolymorphism = true
        }
        )
    }

}
