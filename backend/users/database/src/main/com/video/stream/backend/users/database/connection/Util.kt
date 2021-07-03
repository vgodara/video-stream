package com.video.stream.backend.users.database.connection

import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalSuccess
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.exposed.sql.transactions.transaction

object Util {
    inline fun <Entry> returnFlow(
        crossinline function: () -> Entry?,
    ) = try {
        transaction { function.invoke() }
            ?.let {
                LocalSuccess(it)
            } ?: LocalFailure(NoSuchElementException())
    } catch (cause: Throwable) {
        LocalFailure(cause)
    }.let {
        flowOf(it)
    }.catch { emit(LocalFailure(it)) }
}