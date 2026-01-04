package com.mudrichenkoevgeny.backend.feature.user.database.table

import com.mudrichenkoevgeny.backend.feature.user.enums.UserAccountStatus
import com.mudrichenkoevgeny.backend.feature.user.enums.UserRole
import com.mudrichenkoevgeny.backend.core.database.BaseDbConstraints
import com.mudrichenkoevgeny.backend.core.database.table.BaseTable
import org.jetbrains.exposed.sql.javatime.timestamp

object UsersTable : BaseTable("users") {
    val role = enumerationByName("role", BaseDbConstraints.ENUM_MAX_LENGTH, UserRole::class)
    val accountStatus = enumerationByName("account_status", BaseDbConstraints.ENUM_MAX_LENGTH, UserAccountStatus::class)
    val lastLoginAt = timestamp("last_login_at").nullable()
    val lastActiveAt = timestamp("last_active_at").nullable()

    init {
        index("idx_users_created_at", isUnique = false, createdAt)
    }
}