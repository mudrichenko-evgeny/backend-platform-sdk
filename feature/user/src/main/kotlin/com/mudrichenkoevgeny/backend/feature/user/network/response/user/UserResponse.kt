package com.mudrichenkoevgeny.backend.feature.user.network.response.user

import com.mudrichenkoevgeny.backend.feature.user.network.constants.UserNetworkFields
import com.mudrichenkoevgeny.backend.feature.user.network.response.useridentifier.UserIdentifierResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName(UserNetworkFields.ID)
    val id: String,

    @SerialName(UserNetworkFields.ROLE)
    val role: String,

    @SerialName(UserNetworkFields.ACCOUNT_STATUS)
    val accountStatus: String,

    @SerialName(UserNetworkFields.USER_IDENTIFIERS)
    val userIdentifiers: List<UserIdentifierResponse>,

    @SerialName(UserNetworkFields.LAST_LOGIN_AT)
    val lastLoginAt: Long? = null,

    @SerialName(UserNetworkFields.LAST_ACTIVE_AT)
    val lastActiveAt: Long? = null,

    @SerialName(UserNetworkFields.CREATED_AT)
    val createdAt: Long,

    @SerialName(UserNetworkFields.UPDATED_AT)
    val updatedAt: Long? = null
)