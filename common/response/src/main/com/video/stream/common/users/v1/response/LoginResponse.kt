package com.video.stream.common.users.v1.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val userId: String, val userName: String, val token: String, val refreshToken: String? = null)
