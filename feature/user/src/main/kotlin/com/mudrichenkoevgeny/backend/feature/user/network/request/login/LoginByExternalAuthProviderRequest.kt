package com.mudrichenkoevgeny.backend.feature.user.network.request.login

import com.mudrichenkoevgeny.backend.core.common.validation.NotBlankStringField
import com.mudrichenkoevgeny.backend.feature.user.network.constants.UserNetworkFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginByExternalAuthProviderRequest(
    @NotBlankStringField
    @SerialName(UserNetworkFields.AUTH_PROVIDER)
    val authProvider: String,

    @NotBlankStringField
    @SerialName(UserNetworkFields.TOKEN)
    val token: String
)