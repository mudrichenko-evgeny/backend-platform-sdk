package com.mudrichenkoevgeny.backend.core.storage.di.module

import com.mudrichenkoevgeny.backend.core.common.config.env.EnvReader
import com.mudrichenkoevgeny.backend.core.storage.config.factory.StorageConfigFactory
import com.mudrichenkoevgeny.backend.core.storage.config.factory.StorageConfigFactoryImpl
import com.mudrichenkoevgeny.backend.core.storage.config.model.StorageConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageConfigModule {

    @Provides
    @Singleton
    fun provideStorageConfigFactory(
        envReader: EnvReader
    ): StorageConfigFactory {
        return StorageConfigFactoryImpl(
            envReader = envReader
        )
    }

    @Provides
    @Singleton
    fun provideStorageConfig(
        storageConfigFactory: StorageConfigFactory
    ): StorageConfig {
        return storageConfigFactory.create()
    }
}