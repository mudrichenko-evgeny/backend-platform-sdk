package com.mudrichenkoevgeny.backend.core.database.redisclient

import io.lettuce.core.RedisClient

interface RedisClientCreator {
    fun create(url: String, timeoutSeconds: Long): RedisClient
}