package com.mudrichenkoevgeny.backend.core.audit.di.module

import com.mudrichenkoevgeny.backend.core.audit.database.repository.AuditEventRepository
import com.mudrichenkoevgeny.backend.core.audit.database.repository.AuditEventRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AuditRepositoriesModule {

    @Binds
    @Singleton
    fun bindAuditEventRepository(auditEventRepositoryImpl: AuditEventRepositoryImpl): AuditEventRepository
}