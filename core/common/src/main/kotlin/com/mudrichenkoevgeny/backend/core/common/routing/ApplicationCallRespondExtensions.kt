package com.mudrichenkoevgeny.backend.core.common.routing

import com.mudrichenkoevgeny.backend.core.common.error.parser.AppErrorParser
import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

suspend inline fun <reified T : Any> ApplicationCall.respondResult(
    result: AppResult<T>,
    appErrorParser: AppErrorParser,
    mapper: (T) -> Any = { it }
) {
    when (result) {
        is AppResult.Success -> respond(
            status = HttpStatusCode.OK,
            message = mapper(result.data)
        )
        is AppResult.Error -> respond(
            result.error.httpStatusCode,
            appErrorParser.getApiError(result.error)
        )
    }
}