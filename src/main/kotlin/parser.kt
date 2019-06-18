package io.github.ariesfish.elly

interface Parser {
    fun parse(response: Response): Result
}