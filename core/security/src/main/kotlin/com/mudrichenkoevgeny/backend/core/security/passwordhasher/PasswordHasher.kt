package com.mudrichenkoevgeny.backend.core.security.passwordhasher

interface PasswordHasher {
    fun hash(password: String): String
    fun verify(password: String, storedHash: String): Boolean
    fun isPasswordValid(password: String?, hash: String?): Boolean
}