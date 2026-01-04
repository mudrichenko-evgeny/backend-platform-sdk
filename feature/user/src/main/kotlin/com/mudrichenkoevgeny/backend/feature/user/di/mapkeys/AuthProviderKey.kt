package com.mudrichenkoevgeny.backend.feature.user.di.mapkeys

import com.mudrichenkoevgeny.backend.feature.user.enums.UserAuthProvider
import dagger.MapKey

@MapKey
annotation class AuthProviderKey(val value: UserAuthProvider)