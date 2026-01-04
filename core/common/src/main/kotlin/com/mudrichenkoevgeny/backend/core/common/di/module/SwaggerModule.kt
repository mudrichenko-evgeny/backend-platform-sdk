package com.mudrichenkoevgeny.backend.core.common.di.module

import com.mudrichenkoevgeny.backend.core.common.config.swagger.model.SwaggerConfig
import com.mudrichenkoevgeny.backend.core.common.documentation.swagger.initializer.SwaggerInitializer
import com.mudrichenkoevgeny.backend.core.common.documentation.swagger.initializer.SwaggerInitializerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SwaggerModule {

    @Provides
    @Singleton
    fun provideSwaggerInitializer(
        swaggerConfig: SwaggerConfig
    ): SwaggerInitializer {
        return SwaggerInitializerImpl(
            config = swaggerConfig
        )
    }
}