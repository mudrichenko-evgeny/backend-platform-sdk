package com.mudrichenkoevgeny.backend.core.storage.config.factory

import com.mudrichenkoevgeny.backend.core.storage.config.model.StorageConfig

interface StorageConfigFactory {
    fun create(): StorageConfig
}