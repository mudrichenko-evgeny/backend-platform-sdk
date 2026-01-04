package com.mudrichenkoevgeny.backend.feature.user.route.auth.register

import com.mudrichenkoevgeny.backend.core.common.error.parser.AppErrorParser
import com.mudrichenkoevgeny.backend.core.common.routing.BaseRouter
import com.mudrichenkoevgeny.backend.core.common.routing.respondResult
import com.mudrichenkoevgeny.backend.core.common.validation.validateRequest
import com.mudrichenkoevgeny.backend.feature.user.model.extractClientInfo
import com.mudrichenkoevgeny.backend.feature.user.network.request.register.RegisterByEmailRequest
import com.mudrichenkoevgeny.backend.feature.user.route.auth.AuthRoutes
import com.mudrichenkoevgeny.backend.feature.user.usecase.auth.register.RegisterByEmailUseCase
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import javax.inject.Inject
import javax.inject.Singleton


object RegisterRoutes {
    const val BASE_REGISTER_ROUTE = "/register"
    const val REGISTER_BY_EMAIL = "${AuthRoutes.BASE_AUTH_ROUTE}${BASE_REGISTER_ROUTE}/email"
}

@Singleton
class RegisterRouter @Inject constructor(
    private val appErrorParser: AppErrorParser,
    private val registerByEmailUseCase: RegisterByEmailUseCase
) : BaseRouter {
    override fun register(route: Route) {
        route.post(RegisterRoutes.REGISTER_BY_EMAIL) {
            val request = call.validateRequest<RegisterByEmailRequest>()

            val result = registerByEmailUseCase.execute(
                email = request.email,
                password = request.password,
                confirmationCode = request.confirmationCode,
                clientInfo = call.extractClientInfo()
            )

            call.respondResult(result, appErrorParser)
        }
    }
}