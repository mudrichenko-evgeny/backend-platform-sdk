package com.mudrichenkoevgeny.backend.core.database.config.factory

import com.mudrichenkoevgeny.backend.core.database.config.model.DatabaseConfig

interface DatabaseConfigFactory {
    fun create(): DatabaseConfig
}