package com.mudrichenkoevgeny.backend.feature.user.network.request.login

import com.mudrichenkoevgeny.backend.core.common.validation.NotBlankStringField
import com.mudrichenkoevgeny.backend.feature.user.network.constants.UserNetworkFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginByEmailRequest(
    @NotBlankStringField
    @SerialName(UserNetworkFields.EMAIL)
    val email: String,

    @NotBlankStringField
    @SerialName(UserNetworkFields.PASSWORD)
    val password: String
)