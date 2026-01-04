package com.mudrichenkoevgeny.backend.core.database.redisclient

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LettuceRedisClientCreator @Inject constructor(): RedisClientCreator {

    override fun create(url: String, timeoutSeconds: Long): RedisClient {
        val uri = RedisURI.create(url).apply {
            timeout = Duration.ofSeconds(timeoutSeconds)
        }

        return RedisClient.create(uri)
    }
}