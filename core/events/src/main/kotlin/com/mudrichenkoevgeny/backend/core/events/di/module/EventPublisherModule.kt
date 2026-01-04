package com.mudrichenkoevgeny.backend.core.events.di.module

import com.mudrichenkoevgeny.backend.core.events.publisher.EventPublisher
import com.mudrichenkoevgeny.backend.core.events.publisher.EventPublisherImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface EventPublisherModule {

    @Binds
    @Singleton
    fun bindEventPublisher(eventPublisherImpl: EventPublisherImpl): EventPublisher
}