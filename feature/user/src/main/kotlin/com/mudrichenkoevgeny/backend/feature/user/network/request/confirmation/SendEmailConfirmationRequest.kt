package com.mudrichenkoevgeny.backend.feature.user.network.request.confirmation

import com.mudrichenkoevgeny.backend.core.common.validation.NotBlankStringField
import com.mudrichenkoevgeny.backend.feature.user.network.constants.UserNetworkFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendEmailConfirmationRequest(
    @NotBlankStringField
    @SerialName(UserNetworkFields.EMAIL)
    val email: String
)