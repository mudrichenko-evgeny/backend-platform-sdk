package com.mudrichenkoevgeny.backend.core.database.manager.redis

interface RedisManager {
    suspend fun setWithExpiration(key: String, value: String, expirationSeconds: Long)
    suspend fun incrementWithExpiration(key: String, expirationSeconds: Long): Long
    suspend fun get(key: String): String?
    suspend fun getTtl(key: String): Long
    suspend fun exists(key: String): Boolean
    suspend fun delete(key: String)

    suspend fun isAvailable(): Boolean
    suspend fun warmup()
    fun shutdown()
}