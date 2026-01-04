package com.mudrichenkoevgeny.backend.feature.user.manager.session

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.core.database.utils.dbQuery
import com.mudrichenkoevgeny.backend.feature.user.config.model.UserConfig
import com.mudrichenkoevgeny.backend.feature.user.database.repository.userrefreshtoken.UserRefreshTokenRepository
import com.mudrichenkoevgeny.backend.feature.user.error.model.UserError
import com.mudrichenkoevgeny.backend.feature.user.model.AccessToken
import com.mudrichenkoevgeny.backend.feature.user.model.ClientInfo
import com.mudrichenkoevgeny.backend.feature.user.model.RefreshToken
import com.mudrichenkoevgeny.backend.feature.user.model.SessionToken
import com.mudrichenkoevgeny.backend.feature.user.model.UserId
import com.mudrichenkoevgeny.backend.feature.user.model.UserRefreshToken
import com.mudrichenkoevgeny.backend.feature.user.model.UserRefreshTokenId
import com.mudrichenkoevgeny.backend.feature.user.security.refreshtokenprovider.RefreshTokenProvider
import com.mudrichenkoevgeny.backend.feature.user.security.tokenprovider.TokenProvider
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManagerImpl @Inject constructor(
    private val userConfig: UserConfig,
    private val jwtTokenProvider: TokenProvider,
    private val refreshTokenProvider: RefreshTokenProvider,
    private val userRefreshTokenRepository: UserRefreshTokenRepository
) : SessionManager {

    override suspend fun createSession(
        userId: UserId,
        clientInfo: ClientInfo
    ): AppResult<SessionToken> = dbQuery {
        val now = Instant.now()
        val accessExpiry = now.plus(userConfig.getAccessTokenValidityHoursDuration())
        val refreshExpiry = now.plus(userConfig.getRefreshTokenValidityDaysDuration())

        val accessTokenResult = jwtTokenProvider.generateAccessToken(
            userId = userId,
            issuedAt = now,
            expiration = accessExpiry
        )
        val accessToken = when (accessTokenResult) {
            is AppResult.Success -> accessTokenResult.data
            is AppResult.Error -> return@dbQuery accessTokenResult
        }

        val refreshToken = refreshTokenProvider.getRefreshToken()
        val refreshTokenHash = refreshTokenProvider.getRefreshTokenHash(refreshToken)

        val userRefreshToken = UserRefreshToken(
            id = UserRefreshTokenId(UUID.randomUUID()),
            userId = userId,
            tokenHash = refreshTokenHash,
            expiresAt = refreshExpiry,
            revoked = false,
            userAgent = clientInfo.userAgent,
            ipAddress = clientInfo.ipAddress,
            createdAt = Instant.now(),
            updatedAt = null
        )

        when (val createUserRefreshTokenResult = userRefreshTokenRepository.createUserRefreshToken(userRefreshToken)) {
            is AppResult.Success -> AppResult.Success(
                SessionToken(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    expiresAt = createUserRefreshTokenResult.data.expiresAt
                )
            )
            is AppResult.Error -> createUserRefreshTokenResult
        }
    }

    override suspend fun refreshSession(
        refreshToken: RefreshToken,
        clientInfo: ClientInfo
    ): AppResult<SessionToken> = dbQuery {
        val refreshTokenHash = refreshTokenProvider.getRefreshTokenHash(refreshToken)

        val currentRefreshTokenResult = userRefreshTokenRepository.getUserRefreshTokenByHash(refreshTokenHash)
        val currentRefreshToken = when (currentRefreshTokenResult) {
            is AppResult.Success -> currentRefreshTokenResult.data
            is AppResult.Error -> return@dbQuery currentRefreshTokenResult
        }

        if (currentRefreshToken == null ||
            currentRefreshToken.revoked ||
            currentRefreshToken.expiresAt.isBefore(Instant.now())
        ) {
            return@dbQuery AppResult.Error(UserError.InvalidRefreshToken())
        }

        userRefreshTokenRepository.deleteUserRefreshToken(
            userId = currentRefreshToken.userId,
            refreshTokenHash = refreshTokenHash
        )

        createSession(
            userId = currentRefreshToken.userId,
            clientInfo = clientInfo
        )
    }

    override suspend fun revokeSession(userId: UserId, refreshToken: RefreshToken): AppResult<Unit> = dbQuery {
        val refreshTokenHash = refreshTokenProvider.getRefreshTokenHash(refreshToken)
        userRefreshTokenRepository.deleteUserRefreshToken(
            userId = userId,
            refreshTokenHash = refreshTokenHash
        )
    }

    override suspend fun revokeAllSessions(userId: UserId): AppResult<Unit> = dbQuery {
        userRefreshTokenRepository.deleteAllUserRefreshTokens(userId)
    }

    override suspend fun verifyAccessToken(accessToken: AccessToken): AppResult<UserId> {
        return jwtTokenProvider.verifyAccessToken(accessToken)
    }
}