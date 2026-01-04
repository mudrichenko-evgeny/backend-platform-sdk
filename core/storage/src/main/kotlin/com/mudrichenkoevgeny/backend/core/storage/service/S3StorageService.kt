package com.mudrichenkoevgeny.backend.core.storage.service

import com.mudrichenkoevgeny.backend.core.storage.config.model.StorageConfig
import kotlinx.coroutines.future.await
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class S3StorageService @Inject constructor(
    private val config: StorageConfig
) : StorageService {

    private val s3Client = S3AsyncClient.builder()
        .endpointOverride(URI.create(config.s3Endpoint))
        .region(Region.of(config.s3Region))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(config.s3AccessKey, config.s3SecretKey)
            )
        )
        .forcePathStyle(config.forcePathStyle)
        .build()

    override suspend fun save(
        fileName: String,
        content: ByteArray,
        contentType: String,
        bucket: String?
    ): String {
        val targetBucket = bucket ?: config.s3BucketName

        val request = PutObjectRequest.builder()
            .bucket(targetBucket)
            .key(fileName)
            .contentType(contentType)
            .build()

        s3Client.putObject(request, AsyncRequestBody.fromBytes(content)).await()

        return fileName
    }

    override suspend fun delete(key: String, bucket: String?): Boolean {
        val targetBucket = bucket ?: config.s3BucketName

        val request = DeleteObjectRequest.builder()
            .bucket(targetBucket)
            .key(key)
            .build()

        val response = s3Client.deleteObject(request).await()
        return response.sdkHttpResponse().isSuccessful
    }

    override fun getUrl(key: String): String {
        val baseUrl = config.s3PublicUrl.removeSuffix("/")
        return "$baseUrl/$key"
    }
}