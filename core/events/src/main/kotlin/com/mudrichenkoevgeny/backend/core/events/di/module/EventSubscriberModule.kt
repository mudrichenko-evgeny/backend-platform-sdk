package com.mudrichenkoevgeny.backend.core.events.di.module

import com.mudrichenkoevgeny.backend.core.events.subscriber.EventSubscriber
import com.mudrichenkoevgeny.backend.core.events.subscriber.EventSubscriberImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface EventSubscriberModule {

    @Binds
    @Singleton
    fun bindEventSubscriber(eventSubscriberImpl: EventSubscriberImpl): EventSubscriber
}