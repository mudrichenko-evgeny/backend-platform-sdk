package com.mudrichenkoevgeny.backend.core.events.config.factory

import com.mudrichenkoevgeny.backend.core.events.config.model.EventsConfig

interface EventsConfigFactory {
    fun create(): EventsConfig
}