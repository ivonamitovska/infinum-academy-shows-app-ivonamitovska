package com.infinum.shows_ivona_mitovska.data.response.generic

data class GenericResponse<E>(
    val data: E,
    val errorMsg: String?,
    val responseStatus: ResponseStatus
)