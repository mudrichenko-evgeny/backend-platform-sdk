package com.mudrichenkoevgeny.backend.core.common.config.swagger.factory

import com.mudrichenkoevgeny.backend.core.common.config.swagger.model.SwaggerConfig

interface SwaggerConfigFactory {
    fun create(): SwaggerConfig
}