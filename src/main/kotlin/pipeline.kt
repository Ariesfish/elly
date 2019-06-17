package io.github.ariesfish.elly

interface Pipeline {
    fun process(item: List<String>, request: Request)
}