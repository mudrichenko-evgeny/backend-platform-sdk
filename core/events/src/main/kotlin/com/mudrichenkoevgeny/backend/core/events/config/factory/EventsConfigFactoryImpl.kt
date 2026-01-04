package com.mudrichenkoevgeny.backend.core.events.config.factory

import com.mudrichenkoevgeny.backend.core.common.config.env.EnvReader
import com.mudrichenkoevgeny.backend.core.events.config.envkeys.EventsEnvKeys
import com.mudrichenkoevgeny.backend.core.events.config.model.EventsConfig
import com.mudrichenkoevgeny.backend.core.events.enums.EventsType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsConfigFactoryImpl @Inject constructor(
    private val envReader: EnvReader
) : EventsConfigFactory {

    override fun create(): EventsConfig {
        val eventsType = EventsType.fromString(envReader.getByKey(EventsEnvKeys.EVENTS_TYPE))
        val kafkaBootstrapServers = envReader.getByKey(EventsEnvKeys.KAFKA_BOOTSTRAP_SERVERS)
        val kafkaGroupId = envReader.getByKey(EventsEnvKeys.KAFKA_GROUP_ID)
        val kafkaClientId = envReader.getByKey(EventsEnvKeys.KAFKA_CLIENT_ID)

        return EventsConfig(
            eventsType = eventsType,
            kafkaBootstrapServers = kafkaBootstrapServers,
            kafkaGroupId = kafkaGroupId,
            kafkaClientId = kafkaClientId
        )
    }
}