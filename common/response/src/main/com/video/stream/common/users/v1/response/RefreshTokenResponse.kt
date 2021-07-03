package com.video.stream.common.users.v1.response

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(val token: String)
