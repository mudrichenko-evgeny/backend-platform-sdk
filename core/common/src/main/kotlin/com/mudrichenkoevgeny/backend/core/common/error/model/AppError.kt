package com.mudrichenkoevgeny.backend.core.common.error.model

import io.ktor.http.HttpStatusCode

/**
 * Represents an application error that can be propagated through the layers of the system.
 *
 * This interface distinguishes between information safe to expose to the client
 * (`publicArgs`) and sensitive/internal details (`secretArgs`), while keeping
 * a unique `errorId` for tracking and correlation in logs.
 */
interface AppError {
    val errorId: ErrorId
    val code: String
    val publicArgs: Map<String, Any>?
    val secretArgs: Map<String, Any>?
    val httpStatusCode: HttpStatusCode
    val appErrorSeverity: AppErrorSeverity
}