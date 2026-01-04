package com.mudrichenkoevgeny.backend.feature.user.usecase.auth.login

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.feature.user.error.model.UserError
import com.mudrichenkoevgeny.backend.feature.user.model.ClientInfo
import com.mudrichenkoevgeny.backend.feature.user.network.response.auth.AuthResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginByExternalAuthProviderUseCase @Inject constructor(

) {
    suspend fun execute(
        authProvider: String,
        token: String,
        clientInfo: ClientInfo
    ): AppResult<AuthResponse> {
        // todo
        return AppResult.Error(UserError.ExternalIdMismatch())
    }
}