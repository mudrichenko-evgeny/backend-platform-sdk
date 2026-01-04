package com.mudrichenkoevgeny.backend.core.observability.di.module

import com.mudrichenkoevgeny.backend.core.common.logs.AppLogger
import com.mudrichenkoevgeny.backend.core.observability.config.model.ObservabilityConfig
import com.mudrichenkoevgeny.backend.core.observability.telemetry.TelemetryProvider
import com.mudrichenkoevgeny.backend.core.observability.telemetry.TelemetryProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TelemetryModule {

    @Provides
    @Singleton
    fun provideTelemetryProvider(
        observabilityConfig: ObservabilityConfig,
        appLogger: AppLogger
    ): TelemetryProvider = TelemetryProviderImpl(
        observabilityConfig = observabilityConfig,
        appLogger = appLogger
    )
}