package com.mudrichenkoevgeny.backend.core.common.config.common.factory

import com.mudrichenkoevgeny.backend.core.common.config.common.model.CommonConfig

interface CommonConfigFactory {
    fun create(): CommonConfig
}