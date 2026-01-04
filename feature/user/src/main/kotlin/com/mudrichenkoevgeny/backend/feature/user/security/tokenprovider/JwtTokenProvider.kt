package com.mudrichenkoevgeny.backend.feature.user.security.tokenprovider

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.feature.user.config.model.UserConfig
import com.mudrichenkoevgeny.backend.feature.user.model.AccessToken
import com.mudrichenkoevgeny.backend.feature.user.model.RefreshToken
import com.mudrichenkoevgeny.backend.feature.user.model.RefreshTokenHash
import com.mudrichenkoevgeny.backend.feature.user.error.model.UserError
import com.mudrichenkoevgeny.backend.feature.user.model.UserId
import com.mudrichenkoevgeny.backend.feature.user.security.refreshtokenprovider.RefreshTokenProvider
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.time.Instant
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JwtTokenProvider @Inject constructor(
    userConfig: UserConfig,
    private val refreshTokenProvider: RefreshTokenProvider
) : TokenProvider {

    private val key = Keys.hmacShaKeyFor(userConfig.jwtSecret.toByteArray())

    override fun generateAccessToken(userId: UserId, issuedAt: Instant, expiration: Instant): AppResult<AccessToken> {
        return try {
            val accessToken = Jwts.builder()
                .subject(userId.value.toString())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(key)
                .compact()

            AppResult.Success(AccessToken(accessToken))
        } catch (_: Exception) {
            AppResult.Error(UserError.InvalidAccessToken())
        }
    }

    override fun verifyAccessToken(accessToken: AccessToken): AppResult<UserId> {
        return try {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken.value)

            AppResult.Success(UserId(UUID.fromString(claims.payload.subject)))
        } catch (_: ExpiredJwtException) {
            AppResult.Error(UserError.AccessTokenExpired())
        } catch (_: Exception) {
            AppResult.Error(UserError.InvalidAccessToken())
        }
    }

    override fun generateRefreshToken(): RefreshToken {
        return refreshTokenProvider.getRefreshToken()
    }

    override fun getRefreshTokenHash(refreshToken: RefreshToken): RefreshTokenHash {
        return refreshTokenProvider.getRefreshTokenHash(refreshToken)
    }
}
