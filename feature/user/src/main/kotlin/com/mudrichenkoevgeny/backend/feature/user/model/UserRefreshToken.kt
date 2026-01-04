package com.mudrichenkoevgeny.backend.feature.user.model

import java.time.Instant

data class UserRefreshToken(
    val id: UserRefreshTokenId,
    val userId: UserId,
    val tokenHash: RefreshTokenHash,
    val expiresAt: Instant,
    val revoked: Boolean,
    val userAgent: String? = null,
    val ipAddress: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant?
)