package com.mudrichenkoevgeny.backend.feature.user.service.phone

interface PhoneService {
    fun sendVerificationCode(phoneNumber: String, code: String)
}