package com.mudrichenkoevgeny.backend.feature.user.security.refreshtokenprovider

import com.mudrichenkoevgeny.backend.feature.user.model.RefreshToken
import com.mudrichenkoevgeny.backend.feature.user.model.RefreshTokenHash
import java.security.MessageDigest
import java.util.Base64
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshTokenProviderImpl @Inject constructor(): RefreshTokenProvider {
    override fun getRefreshToken(): RefreshToken {
        return RefreshToken(UUID.randomUUID().toString() + "." + UUID.randomUUID().toString())
    }

    override fun getRefreshTokenHash(refreshToken: RefreshToken): RefreshTokenHash {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(refreshToken.value.toByteArray())
        return RefreshTokenHash(Base64.getEncoder().encodeToString(hash))
    }
}