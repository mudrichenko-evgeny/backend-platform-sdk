package com.mudrichenkoevgeny.backend.feature.user.manager.auth

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.core.common.result.mapNotNullOrError
import com.mudrichenkoevgeny.backend.core.database.utils.dbQuery
import com.mudrichenkoevgeny.backend.feature.user.enums.UserAccountStatus
import com.mudrichenkoevgeny.backend.feature.user.enums.UserAuthProvider
import com.mudrichenkoevgeny.backend.feature.user.enums.UserRole
import com.mudrichenkoevgeny.backend.feature.user.error.model.UserError
import com.mudrichenkoevgeny.backend.feature.user.manager.session.SessionManager
import com.mudrichenkoevgeny.backend.feature.user.manager.user.UserManager
import com.mudrichenkoevgeny.backend.feature.user.manager.useridentifier.UserIdentifierManager
import com.mudrichenkoevgeny.backend.feature.user.mapper.toResponse
import com.mudrichenkoevgeny.backend.feature.user.model.ClientInfo
import com.mudrichenkoevgeny.backend.feature.user.model.UserId
import com.mudrichenkoevgeny.backend.feature.user.model.UserIdentifier
import com.mudrichenkoevgeny.backend.feature.user.network.response.auth.AuthResponse
import com.mudrichenkoevgeny.backend.core.security.passwordhasher.PasswordHasher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManagerImpl @Inject constructor(
    private val passwordHasher: PasswordHasher,
    private val userManager: UserManager,
    private val userIdentifierManager: UserIdentifierManager,
    private val sessionManager: SessionManager
) : AuthManager {

    override suspend fun provideAuthData(
        userId: UserId,
        clientInfo: ClientInfo,
        allowedRoles: Set<UserRole>
    ): AppResult<AuthResponse> = dbQuery {
        val userResult = userManager.getUserById(
            userId = userId
        ).mapNotNullOrError(UserError.UserNotFound())
        val user = when (userResult) {
            is AppResult.Success -> userResult.data
            is AppResult.Error -> return@dbQuery userResult
        }

        if (!allowedRoles.contains(user.role)) {
            return@dbQuery AppResult.Error(UserError.UserForbidden(user.id))
        }

        if (user.accountStatus == UserAccountStatus.BANNED) {
            return@dbQuery AppResult.Error(UserError.UserBlocked(user.id))
        }

        val sessionTokenResult = sessionManager.createSession(
            userId = user.id,
            clientInfo = clientInfo
        )
        val sessionToken = when (sessionTokenResult) {
            is AppResult.Success -> sessionTokenResult.data
            is AppResult.Error -> return@dbQuery sessionTokenResult
        }

        val userIdentifiersListResult = userIdentifierManager.getUserIdentifierListByUserId(userId)

        val userIdentifiersList = if (userIdentifiersListResult is AppResult.Success) {
            userIdentifiersListResult.data
        } else {
            emptyList()
        }

        AppResult.Success(
            AuthResponse(
                userResponse = user.toResponse(userIdentifiersList),
                sessionTokenResponse = sessionToken.toResponse()
            )
        )
    }

    override suspend fun getOrCreateUserIdentifier(
        userAuthProvider: UserAuthProvider,
        identifier: String,
        password: String?,
        userRole: UserRole
    ): AppResult<UserIdentifier> {
        val passwordHash = password?.let { passwordHasher.hash(it) }

        return dbQuery {
            val userIdentifierResult = userIdentifierManager.getUserIdentifier(
                userAuthProvider = userAuthProvider,
                identifier = identifier
            )

            val userIdentifier: UserIdentifier? = when (userIdentifierResult) {
                is AppResult.Success -> userIdentifierResult.data
                is AppResult.Error -> return@dbQuery userIdentifierResult
            }

            if (userIdentifier != null) {
                return@dbQuery AppResult.Success(userIdentifier)
            }

            val createUserResult = userManager.createUser(
                role = userRole
            )
            val user = when (createUserResult) {
                is AppResult.Success -> createUserResult.data
                is AppResult.Error -> return@dbQuery createUserResult
            }

            userIdentifierManager.createUserIdentifier(
                userId = user.id,
                userAuthProvider = userAuthProvider,
                identifier = identifier,
                passwordHash = passwordHash
            )
        }
    }
}