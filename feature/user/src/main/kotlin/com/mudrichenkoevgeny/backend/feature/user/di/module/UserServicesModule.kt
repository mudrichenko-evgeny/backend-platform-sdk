package com.mudrichenkoevgeny.backend.feature.user.di.module

import com.mudrichenkoevgeny.backend.feature.user.service.email.EmailService
import com.mudrichenkoevgeny.backend.feature.user.service.email.EmailServiceImpl
import com.mudrichenkoevgeny.backend.feature.user.service.otp.OtpService
import com.mudrichenkoevgeny.backend.feature.user.service.otp.OtpServiceImpl
import com.mudrichenkoevgeny.backend.feature.user.service.phone.PhoneService
import com.mudrichenkoevgeny.backend.feature.user.service.phone.PhoneServiceImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface UserServicesModule {

    @Binds
    @Singleton
    fun bindOtpService(otpServiceImpl: OtpServiceImpl): OtpService

    @Binds
    @Singleton
    fun bindEmailService(emailServiceImpl: EmailServiceImpl): EmailService

    @Binds
    @Singleton
    fun bindPhoneService(phoneServiceImpl: PhoneServiceImpl): PhoneService
}