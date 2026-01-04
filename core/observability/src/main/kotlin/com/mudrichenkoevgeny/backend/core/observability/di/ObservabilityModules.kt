package com.mudrichenkoevgeny.backend.core.observability.di

import com.mudrichenkoevgeny.backend.core.observability.di.module.ObservabilityConfigModule
import com.mudrichenkoevgeny.backend.core.observability.di.module.TelemetryModule
import dagger.Module

@Module(
    includes = [
        ObservabilityConfigModule::class,
        TelemetryModule::class
    ]
)
interface ObservabilityModules