package com.video.stream.common.users.v1.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val userName: String, val password: String)
