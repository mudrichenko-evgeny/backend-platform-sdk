package com.mudrichenkoevgeny.backend.feature.user.usecase.system

import com.mudrichenkoevgeny.backend.core.common.config.seed.AdminAccount
import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.feature.user.config.model.UserConfig
import com.mudrichenkoevgeny.backend.feature.user.enums.UserAuthProvider
import com.mudrichenkoevgeny.backend.feature.user.enums.UserRole
import com.mudrichenkoevgeny.backend.feature.user.manager.auth.AuthManager
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeedAdminAccountsUseCase @Inject constructor(
    private val userConfig: UserConfig,
    private val authManager: AuthManager
) {
    suspend fun execute(
        adminAccounts: List<AdminAccount> = userConfig.adminAccountsList
    ): AppResult<Unit> = coroutineScope {
        val resultsList = adminAccounts.map { adminAccount ->
            async {
                authManager.getOrCreateUserIdentifier(
                    userAuthProvider = UserAuthProvider.EMAIL,
                    identifier = adminAccount.email,
                    password = adminAccount.password,
                    userRole = UserRole.ADMIN
                )
            }
        }.awaitAll()

        resultsList.filterIsInstance<AppResult.Error>().firstOrNull()?.let { errorResult ->
            return@coroutineScope AppResult.Error(errorResult.error)
        }

        AppResult.Success(Unit)
    }
}