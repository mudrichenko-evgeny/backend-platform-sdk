package com.mudrichenkoevgeny.backend.core.security.ratelimiter

enum class RateLimitAction(
    val id: String,
    val limit: Int,
    val windowSeconds: Int,
    val isIpBased: Boolean
) {
    SEND_OTP("send_otp", limit = 3, windowSeconds = 300, isIpBased = false),

    LOGIN_ATTEMPT("login", limit = 5, windowSeconds = 60, isIpBased = false),

    REGISTRATION_ATTEMPT("registration", limit = 5, windowSeconds = 60, isIpBased = false),

    GLOBAL_AUTH_REQUEST("auth_global", limit = 20, windowSeconds = 60, isIpBased = true),

    REFRESH_TOKEN("refresh", limit = 10, windowSeconds = 60, isIpBased = false);

    fun createKey(identifier: String): String = "rl:$id:$identifier"
}