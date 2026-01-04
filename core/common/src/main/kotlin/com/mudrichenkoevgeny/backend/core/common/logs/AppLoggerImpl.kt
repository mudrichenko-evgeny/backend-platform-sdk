package com.mudrichenkoevgeny.backend.core.common.logs

import com.mudrichenkoevgeny.backend.core.common.di.qualifiers.BusinessLogger
import com.mudrichenkoevgeny.backend.core.common.di.qualifiers.SystemLogger
import com.mudrichenkoevgeny.backend.core.common.error.model.AppError
import com.mudrichenkoevgeny.backend.core.common.error.model.AppErrorSeverity
import com.mudrichenkoevgeny.backend.core.common.error.model.ErrorId
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import org.slf4j.Logger
import javax.inject.Singleton

@Singleton
class AppLoggerImpl(
    @param:SystemLogger private val systemLogger: Logger,
    @param:BusinessLogger private val businessLogger: Logger
) : AppLogger {

    override fun logSystemError(errorId: ErrorId, throwable: Throwable, call: ApplicationCall?) {
        val parts = mutableListOf("Unhandled exception", "errorId=${errorId.value}")

        call?.let {
            parts += "path=${it.request.path()}"
            parts += "method=${it.request.httpMethod.value}"
        }

        val message = parts.joinToString(", ")
        systemLogger.error(message, throwable)
    }

    override fun logBusinessError(appError: AppError) {
        val message = buildString {
            append("Business error, ")
            append("errorId=${appError.errorId.value}, ")
            append("code=${appError.code}, ")
            append("httpStatus=${appError.httpStatusCode.value}, ")
            append("publicArgs=${appError.publicArgs}, ")
            append("secretArgs=${appError.secretArgs}")
        }

        when (appError.appErrorSeverity) {
            AppErrorSeverity.LOW -> businessLogger.info(message)
            AppErrorSeverity.MEDIUM -> businessLogger.warn(message)
            AppErrorSeverity.HIGH -> businessLogger.error(message)
        }
    }
}
