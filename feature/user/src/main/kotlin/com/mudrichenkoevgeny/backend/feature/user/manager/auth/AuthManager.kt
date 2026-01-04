package com.mudrichenkoevgeny.backend.feature.user.manager.auth

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.feature.user.enums.UserAuthProvider
import com.mudrichenkoevgeny.backend.feature.user.enums.UserRole
import com.mudrichenkoevgeny.backend.feature.user.model.ClientInfo
import com.mudrichenkoevgeny.backend.feature.user.model.UserId
import com.mudrichenkoevgeny.backend.feature.user.model.UserIdentifier
import com.mudrichenkoevgeny.backend.feature.user.network.response.auth.AuthResponse

interface AuthManager {
    suspend fun provideAuthData(
        userId: UserId,
        clientInfo: ClientInfo,
        allowedRoles: Set<UserRole>
    ): AppResult<AuthResponse>

    suspend fun getOrCreateUserIdentifier(
        userAuthProvider: UserAuthProvider,
        identifier: String,
        password: String? = null,
        userRole: UserRole = UserRole.USER
    ): AppResult<UserIdentifier>
}