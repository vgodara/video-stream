package com.video.stream.common.users.v1.exception

data class UserException(
    val responseCode: Int,
    override val message: String,
    override val cause: Throwable? = null
) : Throwable(message, cause)
