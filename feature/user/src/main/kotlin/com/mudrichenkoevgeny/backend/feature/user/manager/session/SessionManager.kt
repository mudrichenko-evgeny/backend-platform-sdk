package com.mudrichenkoevgeny.backend.feature.user.manager.session

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.feature.user.model.AccessToken
import com.mudrichenkoevgeny.backend.feature.user.model.ClientInfo
import com.mudrichenkoevgeny.backend.feature.user.model.RefreshToken
import com.mudrichenkoevgeny.backend.feature.user.model.SessionToken
import com.mudrichenkoevgeny.backend.feature.user.model.UserId

interface SessionManager {
    suspend fun createSession(
        userId: UserId,
        clientInfo: ClientInfo
    ): AppResult<SessionToken>

    suspend fun refreshSession(
        refreshToken: RefreshToken,
        clientInfo: ClientInfo
    ): AppResult<SessionToken>

    suspend fun revokeSession(userId: UserId, refreshToken: RefreshToken): AppResult<Unit>
    suspend fun revokeAllSessions(userId: UserId): AppResult<Unit>
    suspend fun verifyAccessToken(accessToken: AccessToken): AppResult<UserId>
}