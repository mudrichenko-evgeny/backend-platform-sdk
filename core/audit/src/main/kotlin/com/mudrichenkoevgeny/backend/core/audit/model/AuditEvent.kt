package com.mudrichenkoevgeny.backend.core.audit.model

import com.mudrichenkoevgeny.backend.core.audit.enums.AuditStatus
import kotlinx.serialization.json.JsonElement

data class AuditEvent(
    val actorId: AuditEventId?,
    val action: String,
    val resource: String,
    val resourceId: String?,
    val status: AuditStatus,
    val metadata: Map<String, JsonElement>,
    val message: String? = null
)