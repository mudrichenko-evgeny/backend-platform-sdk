package com.mudrichenkoevgeny.backend.feature.user.route

import com.mudrichenkoevgeny.backend.core.common.routing.BaseRouter
import com.mudrichenkoevgeny.backend.feature.user.route.auth.AuthRouter
import io.ktor.server.routing.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRouter @Inject constructor(
    private val authRouter: AuthRouter
) : BaseRouter {
    override fun register(route: Route) {
        authRouter.register(route)
        // todo
    }
}