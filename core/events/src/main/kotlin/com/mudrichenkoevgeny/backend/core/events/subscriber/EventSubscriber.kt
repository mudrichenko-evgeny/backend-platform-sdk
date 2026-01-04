package com.mudrichenkoevgeny.backend.core.events.subscriber

import com.mudrichenkoevgeny.backend.core.events.event.AppEvent

interface EventSubscriber {
    fun <T : AppEvent> subscribe(
        topic: String,
        type: Class<T>,
        handler: suspend (event: T, metadata: Map<String, String>) -> Unit
    )
}