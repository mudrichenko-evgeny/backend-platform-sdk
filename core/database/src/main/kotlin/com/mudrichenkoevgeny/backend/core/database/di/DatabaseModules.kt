package com.mudrichenkoevgeny.backend.core.database.di

import com.mudrichenkoevgeny.backend.core.database.di.module.DatabaseConfigModule
import com.mudrichenkoevgeny.backend.core.database.di.module.DatabaseHealthCheckModule
import com.mudrichenkoevgeny.backend.core.database.di.module.DatabaseModule
import com.mudrichenkoevgeny.backend.core.database.di.module.RedisModule
import dagger.Module

@Module(
    includes = [
        DatabaseConfigModule::class,
        DatabaseModule::class,
        RedisModule::class,
        DatabaseHealthCheckModule::class
    ]
)
interface DatabaseModules