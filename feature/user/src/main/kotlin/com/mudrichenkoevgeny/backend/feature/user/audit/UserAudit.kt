package com.mudrichenkoevgeny.backend.feature.user.audit

object UserAudit {
    const val RESOURCE = "USER"

    object Actions {
        const val LOGIN_SUCCESS = "AUTH_LOGIN_SUCCESS"
        const val LOGIN_FAILED = "AUTH_LOGIN_FAILED"
    }

    object Reasons {
        const val USER_NOT_FOUND = "user_not_found"
        const val WRONG_PASSWORD = "wrong_password"
    }

    object Metadata {
        const val REASON = "reason"
        const val IP = "ip"
        const val USER_AGENT = "userAgent"
    }
}