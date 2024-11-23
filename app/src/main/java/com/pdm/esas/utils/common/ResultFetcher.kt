package com.pdm.esas.utils.common

interface ResultFetcher<T : Any> {
    suspend fun fetch(): T?
}

interface ResultFetcherBlocking<T : Any> {
    fun fetch(): T?
}