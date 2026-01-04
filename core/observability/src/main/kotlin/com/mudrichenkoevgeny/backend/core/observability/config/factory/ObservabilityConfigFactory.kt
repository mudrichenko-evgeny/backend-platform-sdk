package com.mudrichenkoevgeny.backend.core.observability.config.factory

import com.mudrichenkoevgeny.backend.core.observability.config.model.ObservabilityConfig

interface ObservabilityConfigFactory {
    fun create(): ObservabilityConfig
}