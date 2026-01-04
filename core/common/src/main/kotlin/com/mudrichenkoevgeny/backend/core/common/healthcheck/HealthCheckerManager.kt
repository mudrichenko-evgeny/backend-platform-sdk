package com.mudrichenkoevgeny.backend.core.common.healthcheck

import com.mudrichenkoevgeny.backend.core.common.error.model.AppError
import com.mudrichenkoevgeny.backend.core.common.error.model.CommonError
import com.mudrichenkoevgeny.backend.core.common.logs.AppLogger
import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class HealthCheckerManager @Inject constructor(
    private val healthChecks: Set<@JvmSuppressWildcards HealthCheck>,
    private val appLogger: AppLogger
) {
    fun verifyCriticalHealth() {
        runBlocking {
            val criticalResult = runCriticalChecks()
            if (criticalResult is AppResult.Error) {
                val appError = criticalResult.error
                val throwable = (appError as? CommonError.Throwable)?.throwable
                    ?: IllegalStateException("Application cannot start, critical health check failed")

                appLogger.logSystemError(appError.errorId, throwable, null)

                throw throwable
            }
        }
    }

    suspend fun checkNonCriticalHealth() {
        val nonCriticalErrors = runNonCriticalChecks()
        nonCriticalErrors.forEach { appError ->
            val throwable = (appError as? CommonError.Throwable)?.throwable
                ?: IllegalStateException("Non critical health check failed")
            appLogger.logSystemError(appError.errorId, throwable, null)
        }
    }

    private suspend fun runCriticalChecks(): AppResult<Unit> = coroutineScope {
        val criticalChecks = healthChecks.filter { it.severity == HealthCheckSeverity.CRITICAL }
        val deferredList = criticalChecks.map { check ->
            async {
                check.check()
            }
        }

        try {
            deferredList.forEach { deferred ->
                val result = deferred.await()
                if (result is AppResult.Error) {
                    deferredList.forEach { it.cancel() }
                    return@coroutineScope result
                }
            }
        } catch (_: CancellationException) { }

        AppResult.Success(Unit)
    }

    private suspend fun runNonCriticalChecks(): List<AppError> {
        val errors = mutableListOf<AppError>()
        coroutineScope {
            healthChecks
                .filter { it.severity == HealthCheckSeverity.NON_CRITICAL }
                .map { healthCheck ->
                    async {
                        val result = healthCheck.check()
                        if (result is AppResult.Error) {
                            errors += result.error
                        }
                    }
                }.awaitAll()
        }
        return errors
    }
}