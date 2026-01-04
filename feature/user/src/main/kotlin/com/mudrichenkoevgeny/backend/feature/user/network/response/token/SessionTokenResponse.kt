package com.mudrichenkoevgeny.backend.feature.user.network.response.token

import com.mudrichenkoevgeny.backend.feature.user.network.constants.UserNetworkFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionTokenResponse(
    @SerialName(UserNetworkFields.ACCESS_TOKEN)
    val accessToken: String,

    @SerialName(UserNetworkFields.REFRESH_TOKEN)
    val refreshToken: String,

    @SerialName(UserNetworkFields.EXPIRES_AT)
    val expiresAt: Long,

    @SerialName(UserNetworkFields.TOKEN_TYPE)
    val tokenType: String
)