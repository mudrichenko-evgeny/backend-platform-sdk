package com.mudrichenkoevgeny.backend.feature.user.service.otp

import com.mudrichenkoevgeny.backend.core.database.manager.redis.RedisManager
import com.mudrichenkoevgeny.backend.feature.user.enums.OtpVerificationType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OtpServiceImpl @Inject constructor(
    private val redisManager: RedisManager
) : OtpService {

    override suspend fun generateOtp(identifier: String, type: OtpVerificationType, expirationSeconds: Long): String {
        val code = (100000..999999).random().toString()

        val key = buildKey(identifier, type)

        redisManager.setWithExpiration(key, code, expirationSeconds)

        return code
    }

    override suspend fun verifyOtp(
        identifier: String,
        type: OtpVerificationType,
        code: String,
        deleteOnSuccess: Boolean
    ): Boolean {
        val key = buildKey(identifier, type)
        val savedCode = redisManager.get(key)

        if (savedCode == null || savedCode != code) {
            return false
        }

        if (deleteOnSuccess) {
            redisManager.delete(key)
        }

        return true
    }

    private fun buildKey(identifier: String, type: OtpVerificationType): String {
        return "otp:${type.name.lowercase()}:$identifier"
    }
}