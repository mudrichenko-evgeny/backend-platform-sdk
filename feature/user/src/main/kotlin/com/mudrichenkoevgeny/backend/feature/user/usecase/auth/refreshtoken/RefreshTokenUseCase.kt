package com.mudrichenkoevgeny.backend.feature.user.usecase.auth.refreshtoken

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.core.security.ratelimiter.RateLimitAction
import com.mudrichenkoevgeny.backend.core.security.ratelimiter.RateLimiter
import com.mudrichenkoevgeny.backend.feature.user.manager.session.SessionManager
import com.mudrichenkoevgeny.backend.feature.user.model.ClientInfo
import com.mudrichenkoevgeny.backend.feature.user.model.RefreshToken
import com.mudrichenkoevgeny.backend.feature.user.model.SessionToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshTokenUseCase @Inject constructor(
    private val rateLimiter: RateLimiter,
    private val sessionManager: SessionManager
) {
    suspend fun execute(
        refreshToken: RefreshToken,
        clientInfo: ClientInfo
    ): AppResult<SessionToken> {
        val rateLimiterResult = rateLimiter.check(
            action = RateLimitAction.REFRESH_TOKEN,
            identifier = refreshToken.value,
            ip = clientInfo.ipAddress
        )
        if (rateLimiterResult is AppResult.Error) {
            return rateLimiterResult
        }

        return sessionManager.refreshSession(
            refreshToken = refreshToken,
            clientInfo = clientInfo
        )
    }
}