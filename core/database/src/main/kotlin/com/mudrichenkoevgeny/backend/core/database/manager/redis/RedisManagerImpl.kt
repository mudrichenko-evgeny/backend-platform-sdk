package com.mudrichenkoevgeny.backend.core.database.manager.redis

import com.mudrichenkoevgeny.backend.core.common.error.model.ErrorId
import com.mudrichenkoevgeny.backend.core.common.logs.AppLogger
import io.lettuce.core.RedisClient
import io.lettuce.core.ScriptOutputType
import io.lettuce.core.api.StatefulRedisConnection
import kotlinx.coroutines.future.await
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RedisManagerImpl @Inject constructor(
    private val redisClient: RedisClient,
    private val appLogger: AppLogger
) : RedisManager {

    @Volatile
    private var connection: StatefulRedisConnection<String, String>? = null
    private val mutex = Mutex()

    private val incrementScript = """
        local current = redis.call('INCR', KEYS[1])
        if current == 1 then
            redis.call('EXPIRE', KEYS[1], ARGV[1])
        end
        return current
    """.trimIndent()

    override suspend fun setWithExpiration(key: String, value: String, expirationSeconds: Long) {
        try {
            getConnection().async().setex(key, expirationSeconds, value).await()
        } catch (e: Exception) {
            appLogger.logSystemError(ErrorId.generate(), e)
        }
    }

    override suspend fun incrementWithExpiration(key: String, expirationSeconds: Long): Long {
        return try {
            getConnection().async().eval<Long>(
                incrementScript,
                ScriptOutputType.INTEGER,
                arrayOf(key),
                expirationSeconds.toString()
            ).await()
        } catch (e: Exception) {
            appLogger.logSystemError(ErrorId.generate(), e)
            0L
        }
    }

    override suspend fun get(key: String): String? {
        return try {
            getConnection().async().get(key).await()
        } catch (e: Exception) {
            appLogger.logSystemError(ErrorId.generate(), e)
            null
        }
    }

    override suspend fun getTtl(key: String): Long {
        return try {
            getConnection().async().ttl(key).await()
        } catch (e: Exception) {
            appLogger.logSystemError(ErrorId.generate(), e)
            -2L
        }
    }

    override suspend fun exists(key: String): Boolean {
        return try {
            getConnection().async().exists(key).await() > 0
        } catch (e: Exception) {
            appLogger.logSystemError(ErrorId.generate(), e)
            false
        }
    }

    override suspend fun delete(key: String) {
        try {
            getConnection().async().del(key).await()
        } catch (e: Exception) {
            appLogger.logSystemError(ErrorId.generate(), e)
        }
    }

    override suspend fun isAvailable(): Boolean {
        return try {
            val response = getConnection().async().ping().await()
            response == PING_RESPONSE
        } catch (e: Exception) {
            appLogger.logSystemError(ErrorId.generate(), e)
            false
        }
    }

    override suspend fun warmup() {
        try {
            getConnection()
        } catch (e: Exception) {
            appLogger.logSystemError(ErrorId.generate(), e)
        }
    }

    override fun shutdown() {
        try {
            connection?.close()
            redisClient.shutdown()
        } catch (e: Exception) {
            appLogger.logSystemError(ErrorId.generate(), e)
        }
    }

    private suspend fun getConnection(): StatefulRedisConnection<String, String> {
        val current = connection
        if (current != null && current.isOpen) {
            return current
        }

        return mutex.withLock {
            val doubleCheck = connection
            if (doubleCheck != null && doubleCheck.isOpen) {
                doubleCheck
            } else {
                connection?.close()
                val newConn = redisClient.connect()
                connection = newConn
                newConn
            }
        }
    }

    companion object {
        private const val PING_RESPONSE = "PONG"
    }
}