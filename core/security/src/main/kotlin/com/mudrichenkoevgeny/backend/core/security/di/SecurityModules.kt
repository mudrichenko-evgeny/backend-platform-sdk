package com.mudrichenkoevgeny.backend.core.security.di

import com.mudrichenkoevgeny.backend.core.security.di.module.PasswordHasherModule
import com.mudrichenkoevgeny.backend.core.security.di.module.RateLimierModule
import dagger.Module


@Module(
    includes = [
        PasswordHasherModule::class,
        RateLimierModule::class
    ]
)
interface SecurityModules