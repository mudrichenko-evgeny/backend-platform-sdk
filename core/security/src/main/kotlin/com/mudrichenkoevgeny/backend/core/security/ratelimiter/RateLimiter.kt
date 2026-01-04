package com.mudrichenkoevgeny.backend.core.security.ratelimiter

import com.mudrichenkoevgeny.backend.core.common.result.AppResult

interface RateLimiter {
    suspend fun check(
        action: RateLimitAction,
        identifier: String,
        ip: String?
    ): AppResult<Unit>
}