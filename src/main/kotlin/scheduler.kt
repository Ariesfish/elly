package io.github.ariesfish.elly

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class Scheduler {
    val pending: BlockingQueue<Request> = LinkedBlockingQueue()
    val result: BlockingQueue<Response> = LinkedBlockingQueue()

    fun addRequest(request: Request) {
        try {
            pending.put(request)
        } catch (e: InterruptedException) {

        }
    }

    fun addResponse(response: Response) {
        try {
            result.put(response)
        } catch (e: InterruptedException) {

        }
    }

    fun hasRequest() = pending.size > 0

    fun hasResponse() = result.size > 0

    fun nextRequest(): Request? {
        return try {
            pending.take()
        } catch (e: InterruptedException) {
            null
        }
    }

    fun nextResponse(): Response? {
        return try {
            result.take()
        } catch (e: InterruptedException) {
            null
        }
    }

    fun addRequests(requests: List<Request>) = requests.forEach { addRequest(it) }

    fun clear() = pending.clear()
}