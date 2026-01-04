package com.mudrichenkoevgeny.backend.feature.user.security.tokenprovider

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.feature.user.model.AccessToken
import com.mudrichenkoevgeny.backend.feature.user.model.RefreshToken
import com.mudrichenkoevgeny.backend.feature.user.model.RefreshTokenHash
import com.mudrichenkoevgeny.backend.feature.user.model.UserId
import java.time.Instant

interface TokenProvider {
    fun generateAccessToken(userId: UserId, issuedAt: Instant, expiration: Instant): AppResult<AccessToken>
    fun verifyAccessToken(accessToken: AccessToken): AppResult<UserId>
    fun generateRefreshToken(): RefreshToken
    fun getRefreshTokenHash(refreshToken: RefreshToken): RefreshTokenHash
}
