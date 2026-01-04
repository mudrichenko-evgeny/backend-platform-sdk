package com.mudrichenkoevgeny.backend.feature.user.database.table

import com.mudrichenkoevgeny.backend.core.database.BaseDbConstraints
import com.mudrichenkoevgeny.backend.core.database.table.BaseTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.timestamp

object UserRefreshTokensTable : BaseTable("user_refresh_tokens") {
    val userId = reference("user_id", UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val tokenHash = text("token_hash")
    val expiresAt = timestamp("expires_at")
    val revoked = bool("revoked").default(false)
    val userAgent = varchar("user_agent", BaseDbConstraints.DEFAULT_MAX_LENGTH).nullable()
    val ipAddress = varchar("ip_address", BaseDbConstraints.IP_MAX_LENGTH).nullable()

    init {
        index("idx_tokens_hash", false, tokenHash)
        index("idx_tokens_user_id", false, userId)
    }
}