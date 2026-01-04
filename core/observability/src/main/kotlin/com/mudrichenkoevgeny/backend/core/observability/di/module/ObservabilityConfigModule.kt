package com.mudrichenkoevgeny.backend.core.observability.di.module

import com.mudrichenkoevgeny.backend.core.common.config.env.EnvReader
import com.mudrichenkoevgeny.backend.core.observability.config.factory.ObservabilityConfigFactory
import com.mudrichenkoevgeny.backend.core.observability.config.factory.ObservabilityConfigFactoryImpl
import com.mudrichenkoevgeny.backend.core.observability.config.model.ObservabilityConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ObservabilityConfigModule {

    @Provides
    @Singleton
    fun provideObservabilityConfigFactory(
        envReader: EnvReader
    ): ObservabilityConfigFactory {
        return ObservabilityConfigFactoryImpl(
            envReader = envReader
        )
    }

    @Provides
    @Singleton
    fun provideObservabilityConfig(
        observabilityConfigFactory: ObservabilityConfigFactory
    ): ObservabilityConfig {
        return observabilityConfigFactory.create()
    }
}