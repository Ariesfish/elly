package io.github.ariesfish.elly.example

import io.github.ariesfish.elly.*
import kotlin.streams.toList

class DoubanSpider(name: String) : Spider(name) {

    init {
        addStartUrls(
            "https://movie.douban.com/tag/爱情",
            "https://movie.douban.com/tag/喜剧",
            "https://movie.douban.com/tag/动画",
            "https://movie.douban.com/tag/动作",
            "https://movie.douban.com/tag/史诗",
            "https://movie.douban.com/tag/犯罪")
    }

    override fun onStart(config: Config) {
        super.onStart(config)

        addPipeline(object : Pipeline {
            override fun process(item: List<String>, request: Request) {
                println(item.toString() + request.toString())
            }
        })
    }

    override fun parse(response: Response): Result {
        val elements = response.body.css("#content table .p12 a")
        val titles: List<String?> = elements.stream().map { it.text() }.toList()

        val result = Result(titles)

        val nextElements = response.body.css("#content > div > div.article > div.paginator > span.next > a")
        val nextPageUrl = nextElements[0].attr("href")
        val nextRequest = makeRequest(nextPageUrl)
        result.addRequest(nextRequest)

        return result
    }
}