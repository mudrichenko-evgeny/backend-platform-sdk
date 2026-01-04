package com.mudrichenkoevgeny.backend.feature.user.database.repository.userrefreshtoken

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.feature.user.model.RefreshTokenHash
import com.mudrichenkoevgeny.backend.feature.user.model.UserId
import com.mudrichenkoevgeny.backend.feature.user.model.UserRefreshToken
import com.mudrichenkoevgeny.backend.feature.user.model.UserRefreshTokenId

interface UserRefreshTokenRepository {
    suspend fun createUserRefreshToken(userRefreshToken: UserRefreshToken): AppResult<UserRefreshToken>
    suspend fun deleteUserRefreshToken(userId: UserId, refreshTokenHash: RefreshTokenHash): AppResult<Unit>
    suspend fun deleteAllUserRefreshTokens(userId: UserId): AppResult<Unit>

    suspend fun revokeToken(refreshTokenHash: RefreshTokenHash): AppResult<Unit>
    suspend fun revokeAllTokensForUser(userId: UserId): AppResult<Unit>

    suspend fun getUserRefreshTokenById(userRefreshTokenId: UserRefreshTokenId): AppResult<UserRefreshToken?>
    suspend fun getUserRefreshTokenByHash(refreshTokenHash: RefreshTokenHash): AppResult<UserRefreshToken?>
    suspend fun getAllUserRefreshTokens(userId: UserId): AppResult<List<UserRefreshToken>>
}