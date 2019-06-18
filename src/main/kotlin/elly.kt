package io.github.ariesfish.elly

import java.lang.RuntimeException
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class Elly private constructor() {
    val spiders = ArrayList<Spider>()
    lateinit var config: Config

    companion object {
        fun me(spider: Spider, config: Config = Config()): Elly {
            val elly = Elly()
            elly.spiders.add(spider)
            elly.config = config
            return elly
        }
    }

    fun start() = EllyEngine(this).start()

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
                        EllyUtils.sleep(100)
                        continue
                    }
                    executorService.submit(Downloader(scheduler, scheduler.nextRequest()))
                }
            }
        }

        downloadThread.isDaemon = true
        downloadThread.name = "download-thread"
        downloadThread.start()
    }

    fun complete() {
        while (isRunning) {
            if (!scheduler.hasRequest()) {
                EllyUtils.sleep(100)
                continue
            }

            val response = scheduler.nextResponse()
            val parser = response?.request?.parser
            if (parser != null) {
                parser
            }
        }
    }
}

class EllyUtils {
    companion object {
        fun sleep(time: Long) {
            try {
                TimeUnit.MILLISECONDS.sleep(time)
            } catch (e: InterruptedException){
                e.printStackTrace()
            }
        }
    }
}