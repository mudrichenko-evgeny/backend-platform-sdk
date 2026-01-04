package com.mudrichenkoevgeny.backend.feature.user.route.auth.login

import com.mudrichenkoevgeny.backend.core.common.error.parser.AppErrorParser
import com.mudrichenkoevgeny.backend.core.common.routing.BaseRouter
import com.mudrichenkoevgeny.backend.core.common.routing.respondResult
import com.mudrichenkoevgeny.backend.core.common.validation.validateRequest
import com.mudrichenkoevgeny.backend.feature.user.model.extractClientInfo
import com.mudrichenkoevgeny.backend.feature.user.network.request.login.LoginByEmailRequest
import com.mudrichenkoevgeny.backend.feature.user.network.request.login.LoginByExternalAuthProviderRequest
import com.mudrichenkoevgeny.backend.feature.user.network.request.login.LoginByPhoneRequest
import com.mudrichenkoevgeny.backend.feature.user.route.auth.AuthRoutes
import com.mudrichenkoevgeny.backend.feature.user.usecase.auth.login.LoginByEmailUseCase
import com.mudrichenkoevgeny.backend.feature.user.usecase.auth.login.LoginByExternalAuthProviderUseCase
import com.mudrichenkoevgeny.backend.feature.user.usecase.auth.login.LoginByPhoneUseCase
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import javax.inject.Inject
import javax.inject.Singleton

object LoginRoutes {
    const val BASE_LOGIN_ROUTE = "/login"
    const val LOGIN_BY_EMAIL = "${AuthRoutes.BASE_AUTH_ROUTE}$BASE_LOGIN_ROUTE/email"
    const val LOGIN_BY_PHONE = "${AuthRoutes.BASE_AUTH_ROUTE}$BASE_LOGIN_ROUTE/phone"
    const val LOGIN_BY_EXTERNAL_AUTH_PROVIDER = "${AuthRoutes.BASE_AUTH_ROUTE}$BASE_LOGIN_ROUTE/external-auth-provider"
}

@Singleton
class LoginRouter @Inject constructor(
    private val appErrorParser: AppErrorParser,
    private val loginByEmailUseCase: LoginByEmailUseCase,
    private val loginByPhoneUseCase: LoginByPhoneUseCase,
    private val loginByExternalAuthProviderUseCase: LoginByExternalAuthProviderUseCase
) : BaseRouter {
    override fun register(route: Route) {
        route.post(LoginRoutes.LOGIN_BY_EMAIL) {
            val request = call.validateRequest<LoginByEmailRequest>()

            val result = loginByEmailUseCase.execute(
                email = request.email,
                password = request.password,
                clientInfo = call.extractClientInfo()
            )

            call.respondResult(result, appErrorParser)
        }

        route.post(LoginRoutes.LOGIN_BY_PHONE) {
            val request = call.validateRequest<LoginByPhoneRequest>()

            val result = loginByPhoneUseCase.execute(
                phoneNumber = request.phoneNumber,
                confirmationCode = request.confirmationCode,
                clientInfo = call.extractClientInfo()
            )

            call.respondResult(result, appErrorParser)
        }

        route.post(LoginRoutes.LOGIN_BY_EXTERNAL_AUTH_PROVIDER) {
            val request = call.validateRequest<LoginByExternalAuthProviderRequest>()

            val result = loginByExternalAuthProviderUseCase.execute(
                authProvider = request.authProvider,
                token = request.token,
                clientInfo = call.extractClientInfo()
            )

            call.respondResult(result, appErrorParser)
        }
    }
}