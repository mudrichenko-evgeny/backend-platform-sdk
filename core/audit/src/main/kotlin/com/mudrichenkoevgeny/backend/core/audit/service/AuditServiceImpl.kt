package com.mudrichenkoevgeny.backend.core.audit.service

import com.mudrichenkoevgeny.backend.core.audit.enums.AuditStatus
import com.mudrichenkoevgeny.backend.core.audit.manager.AuditManager
import com.mudrichenkoevgeny.backend.core.audit.model.AuditEventId
import com.mudrichenkoevgeny.backend.core.common.di.qualifiers.BackgroundScope
import com.mudrichenkoevgeny.backend.core.common.error.model.ErrorId
import com.mudrichenkoevgeny.backend.core.common.logs.AppLogger
import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuditServiceImpl @Inject constructor(
    private val auditManager: AuditManager,
    @param:BackgroundScope private val scope: CoroutineScope,
    private val appLogger: AppLogger
): AuditService {

    override fun log(
        action: String,
        resource: String,
        actorId: AuditEventId?,
        resourceId: String?,
        status: AuditStatus,
        metadata: Map<String, Any?>,
        message: String?
    ) {
        scope.launch {
            try {
                val result = auditManager.createEvent(
                    action = action,
                    resource = resource,
                    actorId = actorId,
                    resourceId = resourceId,
                    status = status,
                    metadata = metadata,
                    message = message
                )

                if (result is AppResult.Error) {
                    appLogger.logSystemError(
                        errorId = result.error.errorId,
                        throwable = RuntimeException("Audit DB error: ${result.error.code}"),
                        call = null
                    )
                }
            } catch (e: Exception) {
                appLogger.logSystemError(
                    errorId = ErrorId.generate(),
                    throwable = e,
                    call = null
                )
            }
        }
    }

    override suspend fun awaitAll() {
        val job = scope.coroutineContext[Job]
        job?.children?.forEach { it.join() }
    }
}