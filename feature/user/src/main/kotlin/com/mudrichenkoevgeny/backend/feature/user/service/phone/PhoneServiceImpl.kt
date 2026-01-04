package com.mudrichenkoevgeny.backend.feature.user.service.phone

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhoneServiceImpl @Inject constructor() : PhoneService {
    override fun sendVerificationCode(phoneNumber: String, code: String) {
        // todo
    }
}