package com.mudrichenkoevgeny.backend.core.storage.service

import com.mudrichenkoevgeny.backend.core.storage.config.model.StorageConfig
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalStorageService @Inject constructor(
    private val config: StorageConfig
) : StorageService {

    private val rootPath = Paths.get(config.localStoragePath).toAbsolutePath().normalize()

    init {
        if (!Files.exists(rootPath)) {
            Files.createDirectories(rootPath)
        }
    }

    override suspend fun save(
        fileName: String,
        content: ByteArray,
        contentType: String,
        bucket: String?
    ): String {
        val targetDir = if (bucket != null) rootPath.resolve(bucket) else rootPath

        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir)
        }

        val targetFile = targetDir.resolve(fileName)
        Files.write(targetFile, content)

        return if (bucket != null) "$bucket/$fileName" else fileName
    }

    override suspend fun delete(key: String, bucket: String?): Boolean {
        val fileToDelete = rootPath.resolve(key).toFile()
        return if (fileToDelete.exists()) {
            fileToDelete.delete()
        } else {
            false
        }
    }

    override fun getUrl(key: String): String {
        return "${config.s3PublicUrl.removeSuffix("/")}/$key"
    }
}