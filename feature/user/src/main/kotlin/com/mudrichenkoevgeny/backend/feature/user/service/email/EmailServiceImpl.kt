package com.mudrichenkoevgeny.backend.feature.user.service.email

import com.mudrichenkoevgeny.backend.feature.user.model.ClientInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmailServiceImpl @Inject constructor() : EmailService {
    override fun sendVerificationCode(email: String, code: String) {
        println("EmailService: sendVerificationCode | $email | $code")
        // todo
    }

    override fun sendResetPasswordVerificationCode(email: String, code: String) {
        println("EmailService: sendResetPasswordVerificationCode | $email | $code")
        // todo
    }

    override fun sendAlreadyRegisteredEmail(email: String, clientInfo: ClientInfo) {
        println("EmailService: sendAlreadyRegisteredEmail | $email")
        // todo
    }
}