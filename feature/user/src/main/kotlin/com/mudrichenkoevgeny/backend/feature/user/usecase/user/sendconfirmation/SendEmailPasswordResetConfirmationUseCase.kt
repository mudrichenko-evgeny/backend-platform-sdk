package com.mudrichenkoevgeny.backend.feature.user.usecase.user.sendconfirmation

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.feature.user.enums.OtpVerificationType
import com.mudrichenkoevgeny.backend.feature.user.model.SendConfirmation
import com.mudrichenkoevgeny.backend.feature.user.service.email.EmailService
import com.mudrichenkoevgeny.backend.feature.user.service.otp.OtpService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SendEmailPasswordResetConfirmationUseCase @Inject constructor(
    private val otpService: OtpService,
    private val emailService: EmailService
) {
    suspend fun execute(
        email: String
    ): AppResult<SendConfirmation> {
        val code = otpService.generateOtp(
            identifier = email,
            type = OtpVerificationType.EMAIL_PASSWORD_RESET
        )

        emailService.sendResetPasswordVerificationCode(email, code)

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