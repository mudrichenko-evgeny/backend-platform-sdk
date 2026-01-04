package com.mudrichenkoevgeny.backend.core.security.ratelimiter

import com.mudrichenkoevgeny.backend.core.common.error.model.CommonError
import com.mudrichenkoevgeny.backend.core.common.error.model.ErrorId
import com.mudrichenkoevgeny.backend.core.common.logs.AppLogger
import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.core.database.manager.redis.RedisManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RateLimiterImpl @Inject constructor(
    private val redisManager: RedisManager,
    private val appLogger: AppLogger
) : RateLimiter {
    override suspend fun check(
        action: RateLimitAction,
        identifier: String,
        ip: String?
    ): AppResult<Unit> {
        return try {
            if (ip != null) {
                val ipResult = checkRaw(RateLimitAction.GLOBAL_AUTH_REQUEST, ip)
                if (ipResult is AppResult.Error) {
                    return ipResult
                }
            }

            checkRaw(action, identifier)
        } catch (e: Exception) {
            appLogger.logSystemError(ErrorId.generate(), e)
            AppResult.Success(Unit)
        }
    }

    private suspend fun checkRaw(action: RateLimitAction, identifier: String): AppResult<Unit> {
        val key = action.createKey(identifier)

        val currentCount = redisManager.incrementWithExpiration(key, action.windowSeconds.toLong())

        if (currentCount > action.limit) {
            val ttl = redisManager.getTtl(key)
            return AppResult.Error(
                CommonError.TooManyRequests(
                    rateLimitActionCode = action.id,
                    limit = action.limit,
                    identifier = key,
                    retryAfterSeconds = if (ttl > 0) {
                        ttl.toInt()
                    } else {
                        action.windowSeconds
                    }
                )
            )
        }
        return AppResult.Success(Unit)
    }
}