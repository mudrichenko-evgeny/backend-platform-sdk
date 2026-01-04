package com.mudrichenkoevgeny.backend.feature.user.route.auth.sendconfirmation

import com.mudrichenkoevgeny.backend.core.common.error.parser.AppErrorParser
import com.mudrichenkoevgeny.backend.core.common.routing.BaseRouter
import com.mudrichenkoevgeny.backend.core.common.routing.respondResult
import com.mudrichenkoevgeny.backend.core.common.validation.validateRequest
import com.mudrichenkoevgeny.backend.feature.user.mapper.toResponse
import com.mudrichenkoevgeny.backend.feature.user.model.extractClientInfo
import com.mudrichenkoevgeny.backend.feature.user.network.request.confirmation.SendEmailConfirmationRequest
import com.mudrichenkoevgeny.backend.feature.user.network.request.confirmation.SendPhoneConfirmationRequest
import com.mudrichenkoevgeny.backend.feature.user.route.UserSwaggerTags
import com.mudrichenkoevgeny.backend.feature.user.route.auth.AuthRoutes
import com.mudrichenkoevgeny.backend.feature.user.usecase.auth.confirmation.SendEmailConfirmationUseCase
import com.mudrichenkoevgeny.backend.feature.user.usecase.auth.confirmation.SendPhoneConfirmationUseCase
import io.github.smiley4.ktoropenapi.config.RouteConfig
import io.github.smiley4.ktoropenapi.post
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import javax.inject.Inject
import javax.inject.Singleton

object SendConfirmationRoutes {
    const val BASE_SEND_CONFIRMATION_ROUTE = "/send-confirmation"
    const val SEND_EMAIL_CONFIRMATION = "${AuthRoutes.BASE_AUTH_ROUTE}$BASE_SEND_CONFIRMATION_ROUTE/email"
    const val SEND_PHONE_CONFIRMATION = "${AuthRoutes.BASE_AUTH_ROUTE}$BASE_SEND_CONFIRMATION_ROUTE/phone"
}

@Singleton
class SendConfirmationRouter @Inject constructor(
    private val appErrorParser: AppErrorParser,
    private val sendEmailConfirmationUseCase: SendEmailConfirmationUseCase,
    private val sendPhoneConfirmationUseCase: SendPhoneConfirmationUseCase
) : BaseRouter {
    override fun register(route: Route) {
        route.post(
            path = SendConfirmationRoutes.SEND_EMAIL_CONFIRMATION,
            builder = { sendEmailConfirmationDocs() },
            body = { sendEmailConfirmation() }
        )

        route.post(
            path = SendConfirmationRoutes.SEND_PHONE_CONFIRMATION,
            builder = { sendPhoneConfirmationDocs() },
            body = { sendPhoneConfirmation() }
        )
    }

    private fun RouteConfig.sendEmailConfirmationDocs() {
        summary = "Send confirmation code via email"
        description = "Initiates email verification process. Generates a code and sends it to the specified address."
        operationId = "sendEmailConfirmation"
        tags = listOf(UserSwaggerTags.AUTH)
        request {
            body<SendEmailConfirmationRequest>()
        }
        response {
            code(HttpStatusCode.OK) {
                description = "Success. Verification code sent."
            }
        }
    }

    private suspend fun RoutingContext.sendEmailConfirmation() {
        val request = call.validateRequest<SendEmailConfirmationRequest>()

        val result = sendEmailConfirmationUseCase.execute(
            email = request.email,
            clientInfo = call.extractClientInfo()
        )

        call.respondResult(result, appErrorParser) {
            sendConfirmation -> sendConfirmation.toResponse()
        }
    }

    private fun RouteConfig.sendPhoneConfirmationDocs() {
        summary = "Send confirmation code via phone"
        description = "Initiates phone verification process. Generates a code and sends it to the specified phone number."
        operationId = "sendPhoneConfirmation"
        tags = listOf(UserSwaggerTags.AUTH)
        request {
            body<SendPhoneConfirmationRequest>()
        }
        response {
            code(HttpStatusCode.OK) {
                description = "Success. Verification code sent."
            }
        }
    }

    private suspend fun RoutingContext.sendPhoneConfirmation() {
        val request = call.validateRequest<SendPhoneConfirmationRequest>()

        val result = sendPhoneConfirmationUseCase.execute(
            phoneNumber = request.phoneNumber,
            call.extractClientInfo()
        )

        call.respondResult(result, appErrorParser) {
            sendConfirmation -> sendConfirmation.toResponse()
        }
    }
}