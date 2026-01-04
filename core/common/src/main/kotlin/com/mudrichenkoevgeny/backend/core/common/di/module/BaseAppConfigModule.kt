package com.mudrichenkoevgeny.backend.core.common.di.module

import com.mudrichenkoevgeny.backend.core.common.config.common.factory.CommonConfigFactory
import com.mudrichenkoevgeny.backend.core.common.config.common.factory.CommonConfigFactoryImpl
import com.mudrichenkoevgeny.backend.core.common.config.common.model.CommonConfig
import com.mudrichenkoevgeny.backend.core.common.config.env.EnvReader
import com.mudrichenkoevgeny.backend.core.common.config.pathresolver.PathResolver
import com.mudrichenkoevgeny.backend.core.common.config.pathresolver.PathResolverImpl
import com.mudrichenkoevgeny.backend.core.common.config.swagger.factory.SwaggerConfigFactory
import com.mudrichenkoevgeny.backend.core.common.config.swagger.factory.SwaggerConfigFactoryImpl
import com.mudrichenkoevgeny.backend.core.common.config.swagger.model.SwaggerConfig
import com.mudrichenkoevgeny.backend.core.common.propertiesprovider.ApplicationPropertiesProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface BaseAppConfigModule {

    @Binds
    @Singleton
    fun bindPathResolver(pathResolverImpl: PathResolverImpl): PathResolver

    companion object {

        @Provides
        @Singleton
        fun provideCommonConfigFactory(
            envReader: EnvReader,
            applicationPropertiesProvider: ApplicationPropertiesProvider
        ): CommonConfigFactory {
            return CommonConfigFactoryImpl(
                envReader = envReader,
                propertiesProvider = applicationPropertiesProvider
            )
        }

        @Provides
        @Singleton
        fun provideCommonConfig(
            commonConfigFactory: CommonConfigFactory
        ): CommonConfig {
            return commonConfigFactory.create()
        }

        @Provides
        @Singleton
        fun provideSwaggerConfigFactory(
            envReader: EnvReader,
            applicationPropertiesProvider: ApplicationPropertiesProvider
        ): SwaggerConfigFactory {
            return SwaggerConfigFactoryImpl(
                envReader = envReader,
                propertiesProvider = applicationPropertiesProvider
            )
        }

        @Provides
        @Singleton
        fun provideSwaggerConfig(
            swaggerConfigFactory: SwaggerConfigFactory
        ): SwaggerConfig {
            return swaggerConfigFactory.create()
        }
    }
}