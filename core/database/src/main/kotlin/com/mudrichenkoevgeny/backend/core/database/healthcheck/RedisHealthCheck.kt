package com.mudrichenkoevgeny.backend.core.database.healthcheck

import com.mudrichenkoevgeny.backend.core.common.error.model.CommonError
import com.mudrichenkoevgeny.backend.core.common.healthcheck.HealthCheck
import com.mudrichenkoevgeny.backend.core.common.healthcheck.HealthCheckSeverity
import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.core.database.manager.redis.RedisManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedisHealthCheck @Inject constructor(
    private val redisManager: RedisManager
) : HealthCheck {

    override val severity: HealthCheckSeverity = HealthCheckSeverity.CRITICAL

    override suspend fun check(): AppResult<Unit> {
        return try {
            if (redisManager.isAvailable()) {
                AppResult.Success(Unit)
            } else {
                AppResult.Error(CommonError.Redis("Redis not available"))
            }
        } catch (e: Exception) {
            AppResult.Error(CommonError.Throwable(e))
        }
    }
}