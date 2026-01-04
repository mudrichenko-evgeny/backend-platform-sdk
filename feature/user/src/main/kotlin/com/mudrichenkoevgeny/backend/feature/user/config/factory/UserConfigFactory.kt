package com.mudrichenkoevgeny.backend.feature.user.config.factory

import com.mudrichenkoevgeny.backend.feature.user.config.model.UserConfig

interface UserConfigFactory {
    fun create(): UserConfig
}