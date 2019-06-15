package io.github.ariesfish.elves

interface Pipeline {
    fun process(item: List<String>, request: Request)
}