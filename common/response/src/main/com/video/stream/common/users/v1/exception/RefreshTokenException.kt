package com.video.stream.common.users.v1.exception

import com.video.stream.common.ResponseCode

data class RefreshTokenException(
    val responseCode: ResponseCode,
    override val message: String,
    override val cause: Throwable? = null
) : Throwable(message, cause)
