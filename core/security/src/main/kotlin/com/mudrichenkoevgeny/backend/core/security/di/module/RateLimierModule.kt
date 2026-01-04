package com.mudrichenkoevgeny.backend.core.security.di.module

import com.mudrichenkoevgeny.backend.core.common.logs.AppLogger
import com.mudrichenkoevgeny.backend.core.database.manager.redis.RedisManager
import com.mudrichenkoevgeny.backend.core.security.ratelimiter.RateLimiter
import com.mudrichenkoevgeny.backend.core.security.ratelimiter.RateLimiterImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RateLimierModule {

    @Provides
    @Singleton
    fun provideRateLimiter(
        redisManager: RedisManager,
        appLogger: AppLogger
    ): RateLimiter {
        return RateLimiterImpl(
            redisManager = redisManager,
            appLogger = appLogger
        )
    }
}