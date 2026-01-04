package com.mudrichenkoevgeny.backend.core.database.di.module

import com.mudrichenkoevgeny.backend.core.common.healthcheck.HealthCheck
import com.mudrichenkoevgeny.backend.core.database.healthcheck.DatabaseHealthCheck
import com.mudrichenkoevgeny.backend.core.database.healthcheck.RedisHealthCheck
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface DatabaseHealthCheckModule {

    @Binds
    @IntoSet
    fun bindDatabaseHealthCheck(databaseHealthCheck: DatabaseHealthCheck): HealthCheck

    @Binds
    @IntoSet
    fun bindRedisHealthCheck(redisHealthCheck: RedisHealthCheck): HealthCheck
}