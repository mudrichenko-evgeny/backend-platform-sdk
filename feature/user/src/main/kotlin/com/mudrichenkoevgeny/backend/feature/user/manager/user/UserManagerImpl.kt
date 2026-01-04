package com.mudrichenkoevgeny.backend.feature.user.manager.user

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.core.database.utils.dbQuery
import com.mudrichenkoevgeny.backend.feature.user.database.repository.user.UserRepository
import com.mudrichenkoevgeny.backend.feature.user.enums.UserAccountStatus
import com.mudrichenkoevgeny.backend.feature.user.enums.UserRole
import com.mudrichenkoevgeny.backend.feature.user.model.User
import com.mudrichenkoevgeny.backend.feature.user.model.UserId
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManagerImpl @Inject constructor(
    private val userRepository: UserRepository
): UserManager {

    override suspend fun getUserById(userId: UserId): AppResult<User?> = dbQuery {
        userRepository.getUserById(userId)
    }

    override suspend fun createUser(role: UserRole, accountStatus: UserAccountStatus): AppResult<User> = dbQuery {
        val now = Instant.now()

        val user = User(
            id = UserId(UUID.randomUUID()),
            role = role,
            accountStatus = accountStatus,
            lastLoginAt = now,
            lastActiveAt = now,
            createdAt = now,
            updatedAt = null
        )

        userRepository.createUser(user)
    }
}