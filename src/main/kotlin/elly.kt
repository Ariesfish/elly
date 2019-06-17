package io.github.ariesfish.elly

import java.lang.RuntimeException
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import kotlin.streams.toList

class Elly {
    val spiders = ArrayList<Spider>()
    lateinit var config: Config

    fun start() {

    }

    fun onStart(consumer: Consumer<Config>) {
        EventManager.registerEvent(EllyEvent.GLOBAL_STARTED, consumer)
    }
}

class EllyEngine(elly: Elly) {
    val spiders = elly.spiders
    val config = elly.config
    val scheduler = Scheduler()
    var isRunning = false
    val executorService = ThreadPoolExecutor(config.parallelThreads, config.parallelThreads, 0, TimeUnit.MILLISECONDS,
        config.queueSize)

    fun start() {
        if (isRunning) {
            throw RuntimeException("Elly is running")
        }

        isRunning = true
        EventManager.fireEvent(EllyEvent.GLOBAL_STARTED, config)

        spiders.forEach { spider ->
            spider.config = config
            val requests: List<Request> = spider.startUrls.asSequence()
                                                    .map { spider.makeRequest(it) }
                                                    .toList() // 使用 sequence 惰性集合操作
            spider.requests.addAll(requests)
            scheduler.addRequests(requests)
        }

        val downloadThread = object : Thread() {
            override fun run() {
                super.run()
                while (isRunning) {
                    if (!scheduler.hasRequest()) {
                        sleep(100)
                        continue
                    }
                    executorService.submit(Downloader(scheduler, scheduler.nextRequest()))
                }
            }
        }
    }
}