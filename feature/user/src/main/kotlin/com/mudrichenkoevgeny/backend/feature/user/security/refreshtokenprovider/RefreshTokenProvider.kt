package com.mudrichenkoevgeny.backend.feature.user.security.refreshtokenprovider

import com.mudrichenkoevgeny.backend.feature.user.model.RefreshToken
import com.mudrichenkoevgeny.backend.feature.user.model.RefreshTokenHash

interface RefreshTokenProvider {
    fun getRefreshToken(): RefreshToken
    fun getRefreshTokenHash(refreshToken: RefreshToken): RefreshTokenHash
}