package com.mudrichenkoevgeny.backend.feature.user.network.response.auth

import com.mudrichenkoevgeny.backend.feature.user.network.constants.UserNetworkFields
import com.mudrichenkoevgeny.backend.feature.user.network.response.token.SessionTokenResponse
import com.mudrichenkoevgeny.backend.feature.user.network.response.user.UserResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName(UserNetworkFields.USER)
    val userResponse: UserResponse,

    @SerialName(UserNetworkFields.SESSION_TOKEN)
    val sessionTokenResponse: SessionTokenResponse
)