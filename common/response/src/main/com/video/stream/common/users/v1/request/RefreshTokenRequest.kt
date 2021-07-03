package com.video.stream.common.users.v1.request

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(val userId: String?=null, val refreshToken: String)
