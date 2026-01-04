package com.mudrichenkoevgeny.backend.core.events.publisher

import com.mudrichenkoevgeny.backend.core.common.serialization.DefaultJson
import com.mudrichenkoevgeny.backend.core.events.config.model.EventsConfig
import com.mudrichenkoevgeny.backend.core.events.event.AppEvent
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.KSerializer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException

@Singleton
class EventPublisherImpl @Inject constructor(
    private val eventsConfig: EventsConfig
) : EventPublisher {

    private val json = DefaultJson

    private val producer: KafkaProducer<String, String> by lazy {
        val props = Properties().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, eventsConfig.kafkaBootstrapServers)
            put(ProducerConfig.CLIENT_ID_CONFIG, eventsConfig.kafkaClientId)
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            put(ProducerConfig.ACKS_CONFIG, "all")
            put(ProducerConfig.RETRIES_CONFIG, 3)
        }
        KafkaProducer<String, String>(props)
    }

    override suspend fun <T : AppEvent> publish(
        topic: String,
        event: T,
        serializer: KSerializer<T>,
        metadata: Map<String, String>
    ) {
        val jsonPayload = json.encodeToString(serializer, event)
        val record = ProducerRecord<String, String>(topic, jsonPayload)

        metadata.forEach { (k, v) ->
            record.headers().add(k, v.toByteArray())
        }

        return suspendCancellableCoroutine { continuation ->
            val future = producer.send(record) { _, exception ->
                if (exception != null) {
                    continuation.resumeWithException(exception)
                } else {
                    continuation.resume(Unit) { _, _, _ ->
                    }
                }
            }

            continuation.invokeOnCancellation {
                future.cancel(true)
            }
        }
    }
}