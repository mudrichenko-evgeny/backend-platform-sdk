package com.mudrichenkoevgeny.backend.feature.user.usecase.auth.login

import com.mudrichenkoevgeny.backend.core.audit.enums.AuditStatus
import com.mudrichenkoevgeny.backend.core.audit.service.AuditService
import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.core.common.result.mapNotNullOrError
import com.mudrichenkoevgeny.backend.core.security.ratelimiter.RateLimitAction
import com.mudrichenkoevgeny.backend.feature.user.enums.UserAuthProvider
import com.mudrichenkoevgeny.backend.feature.user.enums.UserRole
import com.mudrichenkoevgeny.backend.feature.user.error.model.UserError
import com.mudrichenkoevgeny.backend.feature.user.manager.auth.AuthManager
import com.mudrichenkoevgeny.backend.feature.user.manager.useridentifier.UserIdentifierManager
import com.mudrichenkoevgeny.backend.feature.user.model.ClientInfo
import com.mudrichenkoevgeny.backend.feature.user.network.response.auth.AuthResponse
import com.mudrichenkoevgeny.backend.core.security.passwordhasher.PasswordHasher
import com.mudrichenkoevgeny.backend.core.security.ratelimiter.RateLimiter
import com.mudrichenkoevgeny.backend.feature.user.audit.UserAudit
import com.mudrichenkoevgeny.backend.feature.user.audit.logUserAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginByEmailUseCase @Inject constructor(
    private val rateLimiter: RateLimiter,
    private val passwordHasher: PasswordHasher,
    private val userIdentifierManager: UserIdentifierManager,
    private val authManager: AuthManager,
    private val auditService: AuditService
) {
    suspend fun execute(
        email: String,
        password: String,
        clientInfo: ClientInfo
    ): AppResult<AuthResponse> {
        val rateLimiterResult = rateLimiter.check(
            action = RateLimitAction.LOGIN_ATTEMPT,
            identifier = email,
            ip = clientInfo.ipAddress
        )
        if (rateLimiterResult is AppResult.Error) {
            return rateLimiterResult
        }

        val userIdentifierResult = userIdentifierManager.getUserIdentifier(
            userAuthProvider = UserAuthProvider.EMAIL,
            identifier = email
        ).mapNotNullOrError(UserError.InvalidCredentials())

        val userIdentifier = when (userIdentifierResult) {
            is AppResult.Success -> userIdentifierResult.data
            is AppResult.Error -> {
                auditService.logUserAuth(
                    action = UserAudit.Actions.LOGIN_FAILED,
                    clientInfo = clientInfo,
                    reason = UserAudit.Reasons.USER_NOT_FOUND,
                    status = AuditStatus.FAILED
                )
                passwordHasher.isPasswordValid(PASSWORD_FAKE_HASH, password)
                return userIdentifierResult
            }
        }

        if (!passwordHasher.isPasswordValid(userIdentifier.passwordHash, password)) {
            auditService.logUserAuth(
                action = UserAudit.Actions.LOGIN_FAILED,
                userId = userIdentifier.userId,
                clientInfo = clientInfo,
                reason = UserAudit.Reasons.WRONG_PASSWORD,
                status = AuditStatus.FAILED
            )
            return AppResult.Error(UserError.InvalidCredentials())
        }

        val authResult = authManager.provideAuthData(
            userId = userIdentifier.userId,
            clientInfo = clientInfo,
            allowedRoles = setOf(UserRole.USER)
        )

        if (authResult is AppResult.Success) {
            auditService.logUserAuth(
                action = UserAudit.Actions.LOGIN_SUCCESS,
                userId = userIdentifier.userId,
                clientInfo = clientInfo
            )
        }

        return authResult
    }

    companion object {
        const val PASSWORD_FAKE_HASH = "$2a$10N9qo8uLOickgx2ZMRZoMyeIjZAgNIvB.q.2G9S7vV.YtUuVjR5KTu" // todo generate in tests
    }
}