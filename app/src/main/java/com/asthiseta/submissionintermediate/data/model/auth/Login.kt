package com.asthiseta.submissionintermediate.data.model.auth

data class RequestLogin(
    val email: String,
    val password: String
)

data class ResultLogin(
    var name: String,
    var token: String,
    var userId: String
)

data class ResponseLogin(
    val error: Boolean,
    val loginResult: ResultLogin,
    val message: String
)