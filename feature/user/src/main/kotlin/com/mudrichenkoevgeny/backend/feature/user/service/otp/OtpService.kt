package com.mudrichenkoevgeny.backend.feature.user.service.otp

import com.mudrichenkoevgeny.backend.feature.user.enums.OtpVerificationType

interface OtpService {
    suspend fun generateOtp(identifier: String, type: OtpVerificationType, expirationSeconds: Long = 300): String
    suspend fun verifyOtp(
        identifier: String,
        type: OtpVerificationType,
        code: String,
        deleteOnSuccess: Boolean = true
    ): Boolean
}