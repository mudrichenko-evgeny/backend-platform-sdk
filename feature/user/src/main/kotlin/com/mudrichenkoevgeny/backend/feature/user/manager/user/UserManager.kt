package com.mudrichenkoevgeny.backend.feature.user.manager.user

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.feature.user.enums.UserAccountStatus
import com.mudrichenkoevgeny.backend.feature.user.enums.UserRole
import com.mudrichenkoevgeny.backend.feature.user.model.User
import com.mudrichenkoevgeny.backend.feature.user.model.UserId

interface UserManager {
    suspend fun getUserById(
        userId: UserId
    ): AppResult<User?>

    suspend fun createUser(
        role: UserRole = UserRole.USER,
        accountStatus: UserAccountStatus = UserAccountStatus.ACTIVE
    ): AppResult<User>
}