package com.mudrichenkoevgeny.backend.core.common.di

import com.mudrichenkoevgeny.backend.core.common.di.module.BaseAppConfigModule
import com.mudrichenkoevgeny.backend.core.common.di.module.AppErrorParserModule
import com.mudrichenkoevgeny.backend.core.common.di.module.AppLoggerModule
import com.mudrichenkoevgeny.backend.core.common.di.module.CoroutineScopeModule
import com.mudrichenkoevgeny.backend.core.common.di.module.EnvModule
import com.mudrichenkoevgeny.backend.core.common.di.module.SwaggerModule
import dagger.Module

@Module(
    includes = [
        CoroutineScopeModule::class,
        EnvModule::class,
        BaseAppConfigModule::class,
        AppErrorParserModule::class,
        AppLoggerModule::class,
        SwaggerModule::class
    ]
)
interface CommonModules