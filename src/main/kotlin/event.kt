package io.github.ariesfish.elly

import java.util.*
import java.util.function.Consumer
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

enum class EllyEvent {
    GLOBAL_STARTED,
    SPIDER_STARTED
}

class EventManager {
    companion object {
        private val eventConsumerMap: MutableMap<EllyEvent, MutableList<Consumer<Config>>> = HashMap()

        fun registerEvent(ellyEvent: EllyEvent, consumer: Consumer<Config>) {
            var consumers: MutableList<Consumer<Config>>? = eventConsumerMap[ellyEvent]
            if (consumers == null) {
                consumers = ArrayList()
            }
            consumers.add(consumer)
            eventConsumerMap[ellyEvent] = consumers
        }

        fun fireEvent(ellyEvent: EllyEvent, config: Config) {
            eventConsumerMap[ellyEvent]?.forEach { it.accept(config) }
        }
    }
}