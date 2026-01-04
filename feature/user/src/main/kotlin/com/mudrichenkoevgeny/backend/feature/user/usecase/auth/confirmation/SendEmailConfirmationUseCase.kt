package com.mudrichenkoevgeny.backend.feature.user.usecase.auth.confirmation

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.core.security.ratelimiter.RateLimitAction
import com.mudrichenkoevgeny.backend.core.security.ratelimiter.RateLimiter
import com.mudrichenkoevgeny.backend.feature.user.enums.OtpVerificationType
import com.mudrichenkoevgeny.backend.feature.user.enums.UserAuthProvider
import com.mudrichenkoevgeny.backend.feature.user.manager.useridentifier.UserIdentifierManager
import com.mudrichenkoevgeny.backend.feature.user.model.ClientInfo
import com.mudrichenkoevgeny.backend.feature.user.model.SendConfirmation
import com.mudrichenkoevgeny.backend.feature.user.service.email.EmailService
import com.mudrichenkoevgeny.backend.feature.user.service.otp.OtpService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SendEmailConfirmationUseCase @Inject constructor(
    private val rateLimiter: RateLimiter,
    private val otpService: OtpService,
    private val emailService: EmailService,
    private val userIdentifierManager: UserIdentifierManager
) {
    suspend fun execute(
        email: String,
        clientInfo: ClientInfo
    ): AppResult<SendConfirmation> {
        val startTimeNs = System.nanoTime()
        val rateLimiterResult = rateLimiter.check(
            action = RateLimitAction.SEND_OTP,
            identifier = email,
            ip = clientInfo.ipAddress
        )
        if (rateLimiterResult is AppResult.Error) {
            return rateLimiterResult
        }

        val identifierResult = userIdentifierManager.getUserIdentifier(
            userAuthProvider = UserAuthProvider.EMAIL,
            identifier = email
        )

        val existingIdentifier = when (identifierResult) {
            is AppResult.Success -> identifierResult.data
            is AppResult.Error -> return identifierResult
        }

        if (existingIdentifier != null) {
            emailService.sendAlreadyRegisteredEmail(email, clientInfo)
        } else {
            val code = otpService.generateOtp(
                identifier = email,
                type = OtpVerificationType.EMAIL_VERIFICATION
            )

            emailService.sendVerificationCode(email, code)
        }

        return AppResult.Success(
            SendConfirmation(
                retryAfterSeconds = RETRY_AFTER_SECONDS
            )
        )
    }

    companion object {
        const val RETRY_AFTER_SECONDS = 60
    }
}