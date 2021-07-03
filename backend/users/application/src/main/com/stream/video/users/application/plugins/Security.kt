package com.stream.video.users.application.plugins

import com.stream.video.users.application.auth.JwtBuilder
import com.stream.video.users.application.di.KoinModule.JWT_AUDIENCE
import com.stream.video.users.application.di.KoinModule.JWT_REALM
import com.video.stream.backend.users.controller.FindUserByIdController
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val findUser: FindUserByIdController by inject()
    val jwtBuilder: JwtBuilder by inject()
    val jwtAudience: String by inject(named(JWT_AUDIENCE))
    val jwtRealm: String by inject(named(JWT_REALM))
    authentication {
        jwt {
            realm = jwtRealm
            verifier(jwtBuilder.makeTokenJwtVerifier())
            validate { credential ->
                credential.takeIf { credential.payload.audience.contains(jwtAudience) }
                    ?.payload?.getClaim("userId")
                    ?.asString()
                    ?.let { userId ->
                        findUser(userId).filter { it.data==true }
                            .map { JWTPrincipal(credential.payload) }
                            .singleOrNull()

                    }
            }
        }
    }
}

