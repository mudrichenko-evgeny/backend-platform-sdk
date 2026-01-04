package com.mudrichenkoevgeny.backend.feature.user.model

import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.origin
import io.ktor.server.request.userAgent

data class ClientInfo(
    val userAgent: String?,
    val ipAddress: String?
)

fun ApplicationCall.extractClientInfo(): ClientInfo {
    return ClientInfo(
        userAgent = request.userAgent(),
        ipAddress = request.origin.remoteAddress
    )
}