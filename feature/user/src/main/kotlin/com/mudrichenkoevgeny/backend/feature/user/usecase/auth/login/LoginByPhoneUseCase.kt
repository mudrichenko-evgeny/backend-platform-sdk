package com.mudrichenkoevgeny.backend.feature.user.usecase.auth.login

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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginByPhoneUseCase @Inject constructor(
    private val rateLimiter: RateLimiter,
    private val otpService: OtpService,
    private val authManager: AuthManager
) {
    suspend fun execute(
        phoneNumber: String,
        confirmationCode: String,
        clientInfo: ClientInfo
    ): AppResult<AuthResponse> {
        val rateLimiterResult = rateLimiter.check(
            action = RateLimitAction.LOGIN_ATTEMPT,
            identifier = phoneNumber,
            ip = clientInfo.ipAddress
        )
        if (rateLimiterResult is AppResult.Error) {
            return rateLimiterResult
        }

        val isConfirmationCodeCorrect = otpService.verifyOtp(
            identifier = phoneNumber,
            type = OtpVerificationType.PHONE_VERIFICATION,
            code = confirmationCode
        )

        if (!isConfirmationCodeCorrect) {
            return AppResult.Error(UserError.WrongConfirmationCode())
        }

        val userIdentifierResult = authManager.getOrCreateUserIdentifier(
            userAuthProvider = UserAuthProvider.PHONE,
            identifier = phoneNumber,
            password = null,
            userRole = UserRole.USER
        )

        val userIdentifier = when (userIdentifierResult) {
            is AppResult.Success -> userIdentifierResult.data
            is AppResult.Error -> return userIdentifierResult
        }

        return authManager.provideAuthData(
            userId = userIdentifier.userId,
            clientInfo = clientInfo,
            allowedRoles = setOf(UserRole.USER)
        )
    }
}