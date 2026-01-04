package com.mudrichenkoevgeny.backend.core.audit.service

import com.mudrichenkoevgeny.backend.core.audit.enums.AuditStatus
import com.mudrichenkoevgeny.backend.core.audit.model.AuditEventId

interface AuditService {
    fun log(
        action: String,
        resource: String,
        actorId: AuditEventId? = null,
        resourceId: String? = null,
        status: AuditStatus = AuditStatus.SUCCESS,
        metadata: Map<String, Any?> = emptyMap(),
        message: String? = null
    )

    suspend fun awaitAll()
}