package com.stream.video.users.application.auth

import com.video.stream.backend.users.controller.auth.TokenGenerator
import com.video.stream.backend.users.controller.auth.TokenPayload
import com.video.stream.backend.users.controller.auth.TokenVerifier


interface AuthenticationHelper : TokenGenerator, TokenVerifier,TokenPayload,JwtBuilder {

}