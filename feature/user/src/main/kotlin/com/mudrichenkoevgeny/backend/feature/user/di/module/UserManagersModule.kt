package com.mudrichenkoevgeny.backend.feature.user.di.module

import com.mudrichenkoevgeny.backend.feature.user.manager.auth.AuthManager
import com.mudrichenkoevgeny.backend.feature.user.manager.auth.AuthManagerImpl
import com.mudrichenkoevgeny.backend.feature.user.manager.session.SessionManager
import com.mudrichenkoevgeny.backend.feature.user.manager.session.SessionManagerImpl
import com.mudrichenkoevgeny.backend.feature.user.manager.user.UserManager
import com.mudrichenkoevgeny.backend.feature.user.manager.user.UserManagerImpl
import com.mudrichenkoevgeny.backend.feature.user.manager.useridentifier.UserIdentifierManager
import com.mudrichenkoevgeny.backend.feature.user.manager.useridentifier.UserIdentifierManagerImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface UserManagersModule {

    @Binds
    @Singleton
    fun bindUserManager(userManagerImpl: UserManagerImpl): UserManager

    @Binds
    @Singleton
    fun bindUserIdentifierManager(userIdentifierManagerImpl: UserIdentifierManagerImpl): UserIdentifierManager

    @Binds
    @Singleton
    fun bindSessionManager(sessionManagerImpl: SessionManagerImpl): SessionManager

    @Binds
    @Singleton
    fun bindAuthManager(authManagerImpl: AuthManagerImpl): AuthManager
}