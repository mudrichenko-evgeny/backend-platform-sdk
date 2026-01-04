package com.mudrichenkoevgeny.backend.feature.user.network.request.register

import com.mudrichenkoevgeny.backend.core.common.validation.NotBlankStringField
import com.mudrichenkoevgeny.backend.feature.user.network.constants.UserNetworkFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterByEmailRequest(
    @NotBlankStringField
    @SerialName(UserNetworkFields.EMAIL)
    val email: String,

    @NotBlankStringField
    @SerialName(UserNetworkFields.PASSWORD)
    val password: String,

    @NotBlankStringField
    @SerialName(UserNetworkFields.CONFIRMATION_CODE)
    val confirmationCode: String
)