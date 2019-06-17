package io.github.ariesfish.elly

import java.util.function.Consumer
import kotlin.collections.ArrayList

abstract class Spider(val name: String) {
    val startUrls = ArrayList<String>()
    private val pipelines = ArrayList<Pipeline>()
    val requests = ArrayList<Request>()
    lateinit var config: Config

    open fun onStart(_config: Config) {
        config = _config
    }

    abstract fun parse(response: Response): Result

    fun addStartUrls(vararg urls: String) {
        startUrls.addAll(urls)
    }

    fun makeRequest(url: String,
                    parser: (Response) -> Result = this::parse): Request {
        return Request(this, url, parser)
    }

    protected fun addPipeline(pipeline: Pipeline) {
        pipelines.add(pipeline)
    }

    protected fun resetRequest(requestConsumer: Consumer<Request>, requests: List<Request> = this.requests) {
        requests.forEach { requestConsumer.accept(it) }
    }
}