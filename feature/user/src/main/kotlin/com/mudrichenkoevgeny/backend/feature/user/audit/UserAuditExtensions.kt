package com.mudrichenkoevgeny.backend.feature.user.audit

import com.mudrichenkoevgeny.backend.core.audit.enums.AuditStatus
import com.mudrichenkoevgeny.backend.core.audit.model.AuditEventId
import com.mudrichenkoevgeny.backend.core.audit.service.AuditService
import com.mudrichenkoevgeny.backend.feature.user.model.ClientInfo
import com.mudrichenkoevgeny.backend.feature.user.model.UserId

fun AuditService.logUserAuth(
    action: String,
    userId: UserId? = null,
    clientInfo: ClientInfo,
    reason: String? = null,
    status: AuditStatus = AuditStatus.SUCCESS,
) {
    this.log(
        action = action,
        resource = UserAudit.RESOURCE,
        actorId = userId?.let { AuditEventId(it.value) },
        status = status,
        metadata = buildMap {
            put(UserAudit.Metadata.IP, clientInfo.ipAddress)
            put(UserAudit.Metadata.USER_AGENT, clientInfo.userAgent)
            reason?.let { put(UserAudit.Metadata.REASON, it) }
        }
    )
}