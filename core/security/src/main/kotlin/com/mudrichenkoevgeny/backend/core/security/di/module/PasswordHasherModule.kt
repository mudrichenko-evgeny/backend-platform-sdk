package com.mudrichenkoevgeny.backend.core.security.di.module

import com.mudrichenkoevgeny.backend.core.security.passwordhasher.PasswordHasher
import com.mudrichenkoevgeny.backend.core.security.passwordhasher.PasswordHasherImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface PasswordHasherModule {

    @Binds
    @Singleton
    fun bindPasswordHasher(passwordHasherImpl: PasswordHasherImpl): PasswordHasher
}