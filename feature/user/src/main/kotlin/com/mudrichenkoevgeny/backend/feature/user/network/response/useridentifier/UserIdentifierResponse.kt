package com.mudrichenkoevgeny.backend.feature.user.network.response.useridentifier

import com.mudrichenkoevgeny.backend.feature.user.network.constants.UserNetworkFields
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserIdentifierResponse(
    @SerialName(UserNetworkFields.ID)
    val id: String,

    @SerialName(UserNetworkFields.USER_ID)
    val userId: String,

    @SerialName(UserNetworkFields.USER_AUTH_PROVIDER)
    val userAuthProvider: String,

    @SerialName(UserNetworkFields.IDENTIFIER)
    val identifier: String,

    @SerialName(UserNetworkFields.CREATED_AT)
    val createdAt: Long,

    @SerialName(UserNetworkFields.UPDATED_AT)
    val updatedAt: Long? = null
)