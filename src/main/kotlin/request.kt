package io.github.ariesfish.elly

data class Request(val spider: Spider,
                   val url: String,
                   val parser: (Response) -> Result) {
    val method: String="GET"
    val contentType: String="text/html; charset=UTF-8"
    val charset: String="UTF-8"

    val headers = HashMap<String, String>()
    val cookies = HashMap<String, String>()
}