package com.video.stream.utilities

import com.benasher44.uuid.Uuid
import com.lambdaworks.crypto.SCryptUtil

const val EmptyString = ""
private const val saltLength = 10
fun passwordEncoder(password: String, salt: String = EmptyString): String {
    return SCryptUtil.scrypt(password + salt, 16384, 8, 16)
}

fun checkPasswordAgainstHash(password: String, salt: String = EmptyString, passwordHash: String): Boolean {
    return SCryptUtil.check(password + salt, passwordHash)
}

fun generateSalt(length: Int = saltLength): String {
    val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")

}

fun generateUUID(): String {
    return Uuid.randomUUID().toString()
}