package com.mudrichenkoevgeny.backend.core.events.di

import com.mudrichenkoevgeny.backend.core.events.di.module.EventPublisherModule
import com.mudrichenkoevgeny.backend.core.events.di.module.EventSubscriberModule
import com.mudrichenkoevgeny.backend.core.events.di.module.EventsConfigModule
import dagger.Module

@Module(
    includes = [
        EventsConfigModule::class,
        EventPublisherModule::class,
        EventSubscriberModule::class
    ]
)
interface EventsModules