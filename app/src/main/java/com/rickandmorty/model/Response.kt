package com.rickandmorty.model

data class Response<A>(val info: Info, val results: List<A>) {
    data class Info(val pages: Int)
}

data class ErrorResponse(val message: String)