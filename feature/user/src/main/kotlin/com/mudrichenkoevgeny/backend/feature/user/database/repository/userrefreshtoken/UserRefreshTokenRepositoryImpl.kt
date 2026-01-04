package com.mudrichenkoevgeny.backend.feature.user.database.repository.userrefreshtoken

import com.mudrichenkoevgeny.backend.core.common.error.model.CommonError
import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.feature.user.database.table.UserRefreshTokensTable
import com.mudrichenkoevgeny.backend.feature.user.model.RefreshTokenHash
import com.mudrichenkoevgeny.backend.feature.user.model.UserId
import com.mudrichenkoevgeny.backend.feature.user.model.UserRefreshToken
import com.mudrichenkoevgeny.backend.feature.user.model.UserRefreshTokenId
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRefreshTokenRepositoryImpl @Inject constructor() : UserRefreshTokenRepository {

    override suspend fun createUserRefreshToken(
        userRefreshToken: UserRefreshToken
    ): AppResult<UserRefreshToken> {
        val inserted = UserRefreshTokensTable.insert { userRefreshTokenRow ->
            userRefreshTokenRow[id] = userRefreshToken.id.value
            userRefreshTokenRow[userId] = userRefreshToken.userId.value
            userRefreshTokenRow[tokenHash] = userRefreshToken.tokenHash.value
            userRefreshTokenRow[expiresAt] = userRefreshToken.expiresAt
            userRefreshTokenRow[revoked] = userRefreshToken.revoked
            userRefreshTokenRow[userAgent] = userRefreshToken.userAgent
            userRefreshTokenRow[ipAddress] = userRefreshToken.ipAddress
            userRefreshTokenRow[createdAt] = userRefreshToken.createdAt
            userRefreshTokenRow[updatedAt] = userRefreshToken.updatedAt
        }

        if (inserted.insertedCount == 0) {
            return AppResult.Error(
                CommonError.Database(
                    "UserRefreshToken creation failed for userId=${userRefreshToken.userId.value}"
                )
            )
        }

        return AppResult.Success(userRefreshToken)
    }

    override suspend fun deleteUserRefreshToken(
        userId: UserId,
        refreshTokenHash: RefreshTokenHash
    ): AppResult<Unit> {
        UserRefreshTokensTable.deleteWhere {
            (UserRefreshTokensTable.tokenHash eq refreshTokenHash.value) and
                    (UserRefreshTokensTable.userId eq userId.value)
        }

        return AppResult.Success(Unit)
    }

    override suspend fun deleteAllUserRefreshTokens(
        userId: UserId
    ): AppResult<Unit> {
        UserRefreshTokensTable
            .deleteWhere { UserRefreshTokensTable.userId eq userId.value }

        return AppResult.Success(Unit)
    }

    override suspend fun revokeToken(
        refreshTokenHash: RefreshTokenHash
    ): AppResult<Unit> {
        UserRefreshTokensTable
            .update({ UserRefreshTokensTable.tokenHash eq refreshTokenHash.value }) {
                it[UserRefreshTokensTable.revoked] = true
                it[UserRefreshTokensTable.updatedAt] = Instant.now()
            }

        return AppResult.Success(Unit)
    }

    override suspend fun revokeAllTokensForUser(
        userId: UserId
    ): AppResult<Unit> {
        UserRefreshTokensTable
            .update({
                (UserRefreshTokensTable.userId eq userId.value) and
                        (UserRefreshTokensTable.revoked eq false)
            }) {
                it[UserRefreshTokensTable.revoked] = true
                it[UserRefreshTokensTable.updatedAt] = Instant.now()
            }

        return AppResult.Success(Unit)
    }

    override suspend fun getUserRefreshTokenById(
        userRefreshTokenId: UserRefreshTokenId
    ): AppResult<UserRefreshToken?> {
        val resultRow = UserRefreshTokensTable
            .selectAll()
            .where { UserRefreshTokensTable.id eq userRefreshTokenId.value }
            .singleOrNull()

        return AppResult.Success(resultRow?.toUserRefreshToken())
    }

    override suspend fun getUserRefreshTokenByHash(
        refreshTokenHash: RefreshTokenHash
    ): AppResult<UserRefreshToken?> {
        val resultRow = UserRefreshTokensTable
            .selectAll()
            .where { UserRefreshTokensTable.tokenHash eq refreshTokenHash.value }
            .singleOrNull()

        return AppResult.Success(resultRow?.toUserRefreshToken())
    }

    override suspend fun getAllUserRefreshTokens(
        userId: UserId
    ): AppResult<List<UserRefreshToken>> {
        val query = UserRefreshTokensTable
            .selectAll()
            .where { UserRefreshTokensTable.userId eq userId.value }

        val userRefreshTokens = query.map { it.toUserRefreshToken() }

        return AppResult.Success(userRefreshTokens)
    }

    private fun ResultRow.toUserRefreshToken(): UserRefreshToken = UserRefreshToken(
        id = UserRefreshTokenId(this[UserRefreshTokensTable.id].value),
        userId = UserId(this[UserRefreshTokensTable.userId].value),
        tokenHash = RefreshTokenHash(this[UserRefreshTokensTable.tokenHash]),
        expiresAt = this[UserRefreshTokensTable.expiresAt],
        revoked = this[UserRefreshTokensTable.revoked],
        userAgent = this[UserRefreshTokensTable.userAgent],
        ipAddress = this[UserRefreshTokensTable.ipAddress],
        createdAt = this[UserRefreshTokensTable.createdAt],
        updatedAt = this[UserRefreshTokensTable.updatedAt]
    )
}