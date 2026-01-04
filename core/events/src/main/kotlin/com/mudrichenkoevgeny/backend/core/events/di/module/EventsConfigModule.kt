package com.mudrichenkoevgeny.backend.core.events.di.module

import com.mudrichenkoevgeny.backend.core.common.config.env.EnvReader
import com.mudrichenkoevgeny.backend.core.events.config.factory.EventsConfigFactory
import com.mudrichenkoevgeny.backend.core.events.config.factory.EventsConfigFactoryImpl
import com.mudrichenkoevgeny.backend.core.events.config.model.EventsConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class EventsConfigModule {

    @Provides
    @Singleton
    fun provideEventsConfigFactory(
        envReader: EnvReader
    ): EventsConfigFactory {
        return EventsConfigFactoryImpl(
            envReader = envReader
        )
    }

    @Provides
    @Singleton
    fun provideEventsConfig(
        eventsConfigFactory: EventsConfigFactory
    ): EventsConfig {
        return eventsConfigFactory.create()
    }
}