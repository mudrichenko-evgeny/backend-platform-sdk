package com.mudrichenkoevgeny.backend.core.storage.service

interface StorageService {
    suspend fun save(
        fileName: String,
        content: ByteArray,
        contentType: String,
        bucket: String? = null
    ): String

    suspend fun delete(key: String, bucket: String? = null): Boolean

    fun getUrl(key: String): String
}