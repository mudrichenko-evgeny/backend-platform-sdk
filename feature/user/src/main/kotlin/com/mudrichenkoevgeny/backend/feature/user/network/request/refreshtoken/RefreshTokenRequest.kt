package com.mudrichenkoevgeny.backend.feature.user.network.request.refreshtoken

import com.mudrichenkoevgeny.backend.core.common.validation.NotBlankStringField
import com.mudrichenkoevgeny.backend.feature.user.network.constants.UserNetworkFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    @NotBlankStringField
    @SerialName(UserNetworkFields.REFRESH_TOKEN)
    val refreshToken: String
)