package com.mudrichenkoevgeny.backend.feature.user.usecase.auth.confirmation

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.core.security.ratelimiter.RateLimitAction
import com.mudrichenkoevgeny.backend.core.security.ratelimiter.RateLimiter
import com.mudrichenkoevgeny.backend.feature.user.enums.OtpVerificationType
import com.mudrichenkoevgeny.backend.feature.user.model.ClientInfo
import com.mudrichenkoevgeny.backend.feature.user.model.SendConfirmation
import com.mudrichenkoevgeny.backend.feature.user.service.otp.OtpService
import com.mudrichenkoevgeny.backend.feature.user.service.phone.PhoneService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SendPhoneConfirmationUseCase @Inject constructor(
    private val rateLimiter: RateLimiter,
    private val otpService: OtpService,
    private val phoneService: PhoneService
) {
    suspend fun execute(
        phoneNumber: String,
        clientInfo: ClientInfo
    ): AppResult<SendConfirmation> {
        val rateLimiterResult = rateLimiter.check(
            action = RateLimitAction.SEND_OTP,
            identifier = phoneNumber,
            ip = clientInfo.ipAddress
        )
        if (rateLimiterResult is AppResult.Error) {
            return rateLimiterResult
        }

        val code = otpService.generateOtp(
            identifier = phoneNumber,
            type = OtpVerificationType.PHONE_VERIFICATION
        )

        phoneService.sendVerificationCode(phoneNumber, code)

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