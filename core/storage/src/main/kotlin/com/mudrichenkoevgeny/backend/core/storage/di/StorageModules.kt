package com.mudrichenkoevgeny.backend.core.storage.di

import com.mudrichenkoevgeny.backend.core.storage.di.module.StorageConfigModule
import com.mudrichenkoevgeny.backend.core.storage.di.module.StorageServicesModule
import dagger.Module

@Module(
    includes = [
        StorageConfigModule::class,
        StorageServicesModule::class
    ]
)
interface StorageModules