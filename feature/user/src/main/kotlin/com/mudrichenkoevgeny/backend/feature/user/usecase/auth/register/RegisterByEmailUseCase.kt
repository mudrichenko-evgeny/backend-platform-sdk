package com.mudrichenkoevgeny.backend.feature.user.usecase.auth.register

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.core.security.ratelimiter.RateLimitAction
import com.mudrichenkoevgeny.backend.core.security.ratelimiter.RateLimiter
import com.mudrichenkoevgeny.backend.feature.user.enums.OtpVerificationType
import com.mudrichenkoevgeny.backend.feature.user.enums.UserAuthProvider
import com.mudrichenkoevgeny.backend.feature.user.enums.UserRole
import com.mudrichenkoevgeny.backend.feature.user.error.model.UserError
import com.mudrichenkoevgeny.backend.feature.user.manager.auth.AuthManager
import com.mudrichenkoevgeny.backend.feature.user.model.ClientInfo
import com.mudrichenkoevgeny.backend.feature.user.network.response.auth.AuthResponse
import com.mudrichenkoevgeny.backend.feature.user.service.otp.OtpService
import java.time.Duration
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterByEmailUseCase @Inject constructor(
    private val rateLimiter: RateLimiter,
    private val otpService: OtpService,
    private val authManager: AuthManager
) {
    suspend fun execute(
        email: String,
        password: String,
        confirmationCode: String,
        clientInfo: ClientInfo
    ): AppResult<AuthResponse> {
        val rateLimiterResult = rateLimiter.check(
            action = RateLimitAction.REGISTRATION_ATTEMPT,
            identifier = email,
            ip = clientInfo.ipAddress
        )
        if (rateLimiterResult is AppResult.Error) {
            return rateLimiterResult
        }

        val isConfirmationCodeCorrect = otpService.verifyOtp(
            identifier = email,
            type = OtpVerificationType.EMAIL_VERIFICATION,
            code = confirmationCode
        )

        if (!isConfirmationCodeCorrect) {
            return AppResult.Error(UserError.WrongConfirmationCode())
        }

        val userIdentifierResult = authManager.getOrCreateUserIdentifier(
            userAuthProvider = UserAuthProvider.EMAIL,
            identifier = email,
            password = password,
            userRole = UserRole.USER
        )

        val userIdentifier = when (userIdentifierResult) {
            is AppResult.Success -> userIdentifierResult.data
            is AppResult.Error -> return userIdentifierResult
        }

        if (Duration.between(userIdentifier.createdAt, Instant.now()).toMinutes() > MAX_REGISTRATION_WINDOW_MINUTES) {
            return AppResult.Error(UserError.WrongConfirmationCode())
        }

        return authManager.provideAuthData(
            userId = userIdentifier.userId,
            clientInfo = clientInfo,
            allowedRoles = setOf(UserRole.USER)
        )
    }

    companion object {
        const val MAX_REGISTRATION_WINDOW_MINUTES = 5
    }
}