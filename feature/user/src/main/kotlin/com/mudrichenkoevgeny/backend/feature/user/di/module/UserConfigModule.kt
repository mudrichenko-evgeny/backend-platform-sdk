package com.mudrichenkoevgeny.backend.feature.user.di.module

import com.mudrichenkoevgeny.backend.core.common.config.env.EnvReader
import com.mudrichenkoevgeny.backend.feature.user.config.factory.UserConfigFactory
import com.mudrichenkoevgeny.backend.feature.user.config.factory.UserConfigFactoryImpl
import com.mudrichenkoevgeny.backend.feature.user.config.model.UserConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserConfigModule {

    @Provides
    @Singleton
    fun provideUserConfigFactory(
        envReader: EnvReader
    ): UserConfigFactory {
        return UserConfigFactoryImpl(
            envReader = envReader
        )
    }

    @Provides
    @Singleton
    fun provideUserConfig(
        userConfigFactory: UserConfigFactory
    ): UserConfig {
        return userConfigFactory.create()
    }
}