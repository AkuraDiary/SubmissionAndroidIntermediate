package com.asthiseta.submissionintermediate.data.model.auth

data class ResponseRegister(
    val error: Boolean,
    val message: String
)

data class RequestRegister(
    val name:String,
    val email: String,
    val password: String
)