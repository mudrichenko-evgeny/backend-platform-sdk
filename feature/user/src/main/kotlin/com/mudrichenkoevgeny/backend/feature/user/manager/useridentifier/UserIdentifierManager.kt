package com.mudrichenkoevgeny.backend.feature.user.manager.useridentifier

import com.mudrichenkoevgeny.backend.core.common.result.AppResult
import com.mudrichenkoevgeny.backend.feature.user.enums.UserAuthProvider
import com.mudrichenkoevgeny.backend.feature.user.model.UserId
import com.mudrichenkoevgeny.backend.feature.user.model.UserIdentifier

interface UserIdentifierManager {
    suspend fun getUserIdentifier(
        userAuthProvider: UserAuthProvider,
        identifier: String,
    ): AppResult<UserIdentifier?>

    suspend fun getUserIdentifierListByUserId(
        userId: UserId
    ): AppResult<List<UserIdentifier>>

    suspend fun createUserIdentifier(
        userId: UserId,
        userAuthProvider: UserAuthProvider,
        identifier: String,
        passwordHash: String? = null
    ): AppResult<UserIdentifier>
}