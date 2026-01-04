package com.mudrichenkoevgeny.backend.core.common.config.model

import com.mudrichenkoevgeny.backend.core.common.config.enums.AppEnvironment
import com.mudrichenkoevgeny.backend.core.common.config.seed.AdminAccount
import com.mudrichenkoevgeny.backend.core.common.config.swagger.model.SwaggerConfig

// todo delete
data class AppConfig(
    // common module
    val environment: AppEnvironment,
    val version: String,
    val appName: String,
    val ktorServerHost: String,
    val ktorServerPort: Int,
    val serverUrl: String,
    // database module
    val dbUrl: String,
    val dbUser: String,
    val dbPassword: String,
    val migrationPaths: List<String> = listOf("classpath:db/migration"),
    val redisUrl: String,
    val redisTimeoutSeconds: Long,
    // user module
    val jwtSecret: String,
    val accessTokenValidityHours: Long,
    val refreshTokenValidityDays: Long,
    val authRealm: String,
    val domain: String,
    val adminAccountsList: List<AdminAccount>,
    // observability module
    val telemetryServiceName: String,
    val telemetryEndpoint: String,
    val metricIntervalSeconds: Int,
    // common module
    val swaggerConfig: SwaggerConfig
)