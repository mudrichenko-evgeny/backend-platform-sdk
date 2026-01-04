package com.mudrichenkoevgeny.backend.feature.user.manager.useridentifier

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.core.database.utils.dbQuery
import com.mudrichenkoevgeny.backend.feature.user.database.repository.useridentifier.UserIdentifierRepository
import com.mudrichenkoevgeny.backend.feature.user.enums.UserAuthProvider
import com.mudrichenkoevgeny.backend.feature.user.model.UserId
import com.mudrichenkoevgeny.backend.feature.user.model.UserIdentifier
import com.mudrichenkoevgeny.backend.feature.user.model.UserIdentifierId
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserIdentifierManagerImpl @Inject constructor(
    private val userIdentifierRepository: UserIdentifierRepository
): UserIdentifierManager {

    override suspend fun getUserIdentifier(
        userAuthProvider: UserAuthProvider,
        identifier: String
    ): AppResult<UserIdentifier?> = dbQuery {
        userIdentifierRepository.getUserIdentifier(
            userAuthProvider = userAuthProvider,
            identifier = identifier
        )
    }

    override suspend fun getUserIdentifierListByUserId(userId: UserId): AppResult<List<UserIdentifier>> = dbQuery {
        userIdentifierRepository.getUserIdentifiersListByUserId(
            userId = userId
        )
    }

    override suspend fun createUserIdentifier(
        userId: UserId,
        userAuthProvider: UserAuthProvider,
        identifier: String,
        passwordHash: String?
    ): AppResult<UserIdentifier> = dbQuery {
        val now = Instant.now()

        val userIdentifier = UserIdentifier(
            id = UserIdentifierId(UUID.randomUUID()),
            userId = userId,
            userAuthProvider = userAuthProvider,
            identifier = identifier,
            passwordHash = passwordHash,
            createdAt = now,
            updatedAt = null
        )

        userIdentifierRepository.createUserIdentifier(userIdentifier)
    }
}