package com.video.stream.backend.users.datasource

import com.video.stream.common.LocalFailure
import com.video.stream.common.LocalResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

object Logger {
    inline fun <Entity> logError(function: () -> Flow<LocalResponse<Entity>>) =
        function.invoke().catch { emit(LocalFailure(it)) }
}