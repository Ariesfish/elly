package io.github.ariesfish.elves

class Config(var timeout: Int = 10_000,
             var delay: Int = 1000,
             val parallelThreads: Int,
             val userAgent: UserAgent = UserAgent.CHROME_FOR_MAC,
             val queueSize : Int) : Cloneable {

    override fun clone(): Any = super.clone()
}

enum class UserAgent(val agentName: String) {
    SAFARI_FOR_MAC("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50"),
    FIREFOX_FOR_MAC("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1"),
    OPERA_FOR_MAC("Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11"),
    CHROME_FOR_MAC("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11"),
    IE_9_FOR_WIN("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0"),
    IE_8_FOR_WIN("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)")
}