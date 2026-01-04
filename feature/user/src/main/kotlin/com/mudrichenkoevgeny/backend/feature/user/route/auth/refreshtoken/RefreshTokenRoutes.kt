package com.mudrichenkoevgeny.backend.feature.user.route.auth.refreshtoken

import com.mudrichenkoevgeny.backend.core.common.error.parser.AppErrorParser
import com.mudrichenkoevgeny.backend.core.common.routing.BaseRouter
import com.mudrichenkoevgeny.backend.core.common.routing.respondResult
import com.mudrichenkoevgeny.backend.core.common.validation.validateRequest
import com.mudrichenkoevgeny.backend.feature.user.mapper.toResponse
import com.mudrichenkoevgeny.backend.feature.user.model.RefreshToken
import com.mudrichenkoevgeny.backend.feature.user.model.extractClientInfo
import com.mudrichenkoevgeny.backend.feature.user.network.request.refreshtoken.RefreshTokenRequest
import com.mudrichenkoevgeny.backend.feature.user.route.UserSwaggerTags
import com.mudrichenkoevgeny.backend.feature.user.route.auth.AuthRoutes
import com.mudrichenkoevgeny.backend.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.github.smiley4.ktoropenapi.config.RouteConfig
import io.github.smiley4.ktoropenapi.post
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import javax.inject.Inject
import javax.inject.Singleton

object RefreshTokenRoutes {
    const val REFRESH = "${AuthRoutes.BASE_AUTH_ROUTE}/refresh"
}

@Singleton
class RefreshTokenRouter @Inject constructor(
    private val appErrorParser: AppErrorParser,
    private val refreshTokenUseCase: RefreshTokenUseCase
) : BaseRouter {
    override fun register(route: Route) {
        route.post(
            path = RefreshTokenRoutes.REFRESH,
            builder = { refreshTokenDocs() },
            body = { refreshToken() }
        )
    }

    private fun RouteConfig.refreshTokenDocs() {
        summary = "refresh auth token"
        description = "Initiates refresh token process."
        operationId = "refreshToken"
        tags = listOf(UserSwaggerTags.AUTH)
        request {
            body<RefreshTokenRequest>()
        }
        response {
            code(HttpStatusCode.OK) {
                description = "Success. Token refreshed."
            }
        }
    }

    private suspend fun RoutingContext.refreshToken() {
        val request = call.validateRequest<RefreshTokenRequest>()

        val result = refreshTokenUseCase.execute(
            refreshToken = RefreshToken(request.refreshToken),
            clientInfo = call.extractClientInfo()
        )

        call.respondResult(result, appErrorParser) {
            sessionToken -> sessionToken.toResponse()
        }
    }
}