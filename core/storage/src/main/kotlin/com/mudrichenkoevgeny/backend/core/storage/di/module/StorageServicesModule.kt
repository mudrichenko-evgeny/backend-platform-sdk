package com.mudrichenkoevgeny.backend.core.storage.di.module

import com.mudrichenkoevgeny.backend.core.storage.config.model.StorageConfig
import com.mudrichenkoevgeny.backend.core.storage.enums.StorageType
import com.mudrichenkoevgeny.backend.core.storage.service.LocalStorageService
import com.mudrichenkoevgeny.backend.core.storage.service.S3StorageService
import com.mudrichenkoevgeny.backend.core.storage.service.StorageService
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
class StorageServicesModule {

    @Provides
    @Singleton
    fun provideStorageService(
        storageConfig: StorageConfig,
        localStorage: Provider<LocalStorageService>,
        s3Storage: Provider<S3StorageService>
    ): StorageService {
        return when (storageConfig.storageType) {
            StorageType.LOCAL -> localStorage.get()
            StorageType.S3 -> s3Storage.get()
        }
    }
}