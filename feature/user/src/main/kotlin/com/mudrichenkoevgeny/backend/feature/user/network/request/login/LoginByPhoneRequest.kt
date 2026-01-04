package com.mudrichenkoevgeny.backend.feature.user.network.request.login

import com.mudrichenkoevgeny.backend.core.common.validation.NotBlankStringField
import com.mudrichenkoevgeny.backend.feature.user.network.constants.UserNetworkFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginByPhoneRequest(
    @NotBlankStringField
    @SerialName(UserNetworkFields.PHONE_NUMBER)
    val phoneNumber: String,

    @NotBlankStringField
    @SerialName(UserNetworkFields.CONFIRMATION_CODE)
    val confirmationCode: String
)