package com.mudrichenkoevgeny.backend.core.common.config.swagger.factory

import com.mudrichenkoevgeny.backend.core.common.config.common.envkeys.CommonEnvKeys
import com.mudrichenkoevgeny.backend.core.common.config.env.EnvReader
import com.mudrichenkoevgeny.backend.core.common.config.swagger.envkeys.SwaggerEnvKeys
import com.mudrichenkoevgeny.backend.core.common.config.swagger.model.SwaggerConfig
import com.mudrichenkoevgeny.backend.core.common.propertiesprovider.ApplicationPropertiesProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SwaggerConfigFactoryImpl @Inject constructor(
    private val envReader: EnvReader,
    private val propertiesProvider: ApplicationPropertiesProvider
): SwaggerConfigFactory {

    override fun create(): SwaggerConfig {
        val version = propertiesProvider.version

        val serverUrl = envReader.getByKey(CommonEnvKeys.SERVER_URL)
        val title = envReader.getByKey(SwaggerEnvKeys.SWAGGER_TITLE)
        val description = envReader.getByKey(SwaggerEnvKeys.SWAGGER_DESCRIPTION)
        val serverDescription = envReader.getByKey(SwaggerEnvKeys.SWAGGER_SERVER_DESCRIPTION)

        return SwaggerConfig(
            title = title,
            description = description,
            version = version,
            serverUrl = serverUrl,
            serverDescription = serverDescription
        )
    }
}