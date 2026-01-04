package com.mudrichenkoevgeny.backend.core.audit.di

import com.mudrichenkoevgeny.backend.core.audit.di.module.AuditManagersModule
import com.mudrichenkoevgeny.backend.core.audit.di.module.AuditRepositoriesModule
import com.mudrichenkoevgeny.backend.core.audit.di.module.AuditServicesModule
import dagger.Module

@Module(
    includes = [
        AuditRepositoriesModule::class,
        AuditManagersModule::class,
        AuditServicesModule::class
    ]
)
interface AuditModules