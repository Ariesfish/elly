package io.github.ariesfish.elly

import java.io.InputStream
import io.github.biezhi.request.Request as OMYRequest

class Downloader(val scheduler: Scheduler, val request: Request?) : Runnable {
    override fun run() {
        if (request != null) {

            val httpRequest =
                when (request.method.toUpperCase()) {
                    "GET" -> OMYRequest.get(request.url)
                    "POST" -> OMYRequest.post(request.url)
                    else -> null
                }

            val result: InputStream? = httpRequest?.apply {
                contentType(request.contentType)
                headers(request.headers)
                connectTimeout(request.spider.config.timeout)
                readTimeout(request.spider.config.timeout)
            }?.stream()

            if (result != null) {
                scheduler.addResponse(Response(request, result))
            }
        }
    }
}