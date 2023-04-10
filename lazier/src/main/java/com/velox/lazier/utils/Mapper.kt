package com.velox.lazier.utils

interface Map<I, O> {
    fun map(input: I): O
}

interface Mapper<I, O> {
    fun mapTo(input: I): O
    fun mapFrom(input: O): I
}
//cr velox