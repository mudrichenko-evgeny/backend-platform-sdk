package com.mudrichenkoevgeny.backend.core.database.di.module

import com.mudrichenkoevgeny.backend.core.common.config.env.EnvReader
import com.mudrichenkoevgeny.backend.core.database.config.factory.DatabaseConfigFactory
import com.mudrichenkoevgeny.backend.core.database.config.factory.DatabaseConfigFactoryImpl
import com.mudrichenkoevgeny.backend.core.database.config.model.DatabaseConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseConfigModule {

    @Provides
    @Singleton
    fun provideDatabaseConfigFactory(
        envReader: EnvReader
    ): DatabaseConfigFactory {
        return DatabaseConfigFactoryImpl(
            envReader = envReader
        )
    }

    @Provides
    @Singleton
    fun provideDatabaseConfig(
        databaseConfigFactory: DatabaseConfigFactory
    ): DatabaseConfig {
        return databaseConfigFactory.create()
    }
}