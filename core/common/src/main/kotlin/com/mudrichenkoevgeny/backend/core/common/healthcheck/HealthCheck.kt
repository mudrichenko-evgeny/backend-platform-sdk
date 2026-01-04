package com.mudrichenkoevgeny.backend.core.common.healthcheck

import com.mudrichenkoevgeny.backend.core.common.result.AppResult

interface HealthCheck {
    val severity: HealthCheckSeverity
    suspend fun check(): AppResult<Unit>
}