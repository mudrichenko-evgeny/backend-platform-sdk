package com.mudrichenkoevgeny.backend.feature.user.mapper

import com.mudrichenkoevgeny.backend.feature.user.model.SessionToken
import com.mudrichenkoevgeny.backend.feature.user.network.response.token.SessionTokenResponse

fun SessionToken.toResponse(): SessionTokenResponse = SessionTokenResponse(
    accessToken = accessToken.value,
    refreshToken = refreshToken.value,
    expiresAt = expiresAt.toEpochMilli(),
    tokenType = tokenType
)