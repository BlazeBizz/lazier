package com.velox.lazier.utils

interface Mapper<I, O> {
    fun map(input: I): O
}
//cr velox