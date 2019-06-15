package io.github.ariesfish.elves

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import us.codecraft.xsoup.XElements
import us.codecraft.xsoup.Xsoup
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

data class Response(val request: Request, val body: Body)

data class Body(val inputStream: InputStream,
                val charset: String) {

    val text: String by lazy

    override fun toString(): String {
        if (text == null) {
            val html = StringBuilder(100)
            try {
                val br = BufferedReader(InputStreamReader(inputStream, charset))
                var line: String
                do {
                    line = br.readLine()
                    if (line != null) html.append(line).append("\n")
                } while (line != null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            text = html.toString()
        }
        return text
    }

    fun css(css: String): Elements {
        return Jsoup.parse(this.toString()).select(css)
    }

    fun xpath(xpath: String): XElements {
        return Xsoup.compile(xpath).evaluate(Jsoup.parse(this.toString()))
    }
}

class Result(_item: List<String?>) {
    val item = _item
    private val requests = ArrayList<Request>()

    fun addRequest(request: Request) {
        requests.add(request)
    }

    /*fun addRequests(requests: ArrayList<Request>) {
        requests.addAll(requests)
    }*/
}