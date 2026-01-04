package com.mudrichenkoevgeny.backend.core.common.logs

import com.mudrichenkoevgeny.backend.core.common.error.model.AppError
import com.mudrichenkoevgeny.backend.core.common.error.model.ErrorId
import io.ktor.server.application.ApplicationCall

interface AppLogger {
    fun logSystemError(errorId: ErrorId, throwable: Throwable, call: ApplicationCall? = null)
    fun logBusinessError(appError: AppError)

    companion object {
        const val SYSTEM_LOGGER = "system"
        const val BUSINESS_LOGGER = "business"
    }
}