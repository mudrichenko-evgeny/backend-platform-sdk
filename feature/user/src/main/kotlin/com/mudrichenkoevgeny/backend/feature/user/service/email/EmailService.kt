package com.mudrichenkoevgeny.backend.feature.user.service.email

import com.mudrichenkoevgeny.backend.feature.user.model.ClientInfo

interface EmailService {
    fun sendVerificationCode(email: String, code: String)
    fun sendResetPasswordVerificationCode(email: String, code: String)
    fun sendAlreadyRegisteredEmail(email: String, clientInfo: ClientInfo)
}