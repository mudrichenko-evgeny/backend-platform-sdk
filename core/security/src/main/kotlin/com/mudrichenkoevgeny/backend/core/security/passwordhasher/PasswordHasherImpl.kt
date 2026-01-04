package com.mudrichenkoevgeny.backend.core.security.passwordhasher

import com.password4j.Password
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordHasherImpl @Inject constructor(): PasswordHasher {
    override fun hash(password: String): String =
        Password.hash(password)
            .addRandomSalt()
            .withArgon2()
            .result

    override fun verify(password: String, storedHash: String): Boolean =
        Password.check(password, storedHash).withArgon2()

    override fun isPasswordValid(password: String?, hash: String?): Boolean {
        if (password.isNullOrEmpty() || hash.isNullOrEmpty()) {
            return false
        }

        return verify(password, hash)
    }
}