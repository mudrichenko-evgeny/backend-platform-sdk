package com.mudrichenkoevgeny.backend.core.audit.di.module

import com.mudrichenkoevgeny.backend.core.audit.manager.AuditManager
import com.mudrichenkoevgeny.backend.core.audit.manager.AuditManagerImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AuditManagersModule {

    @Binds
    @Singleton
    fun bindAuditManager(auditManagerImpl: AuditManagerImpl): AuditManager
}