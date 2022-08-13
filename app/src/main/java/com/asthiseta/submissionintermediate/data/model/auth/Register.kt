package com.asthiseta.submissionintermediate.data.model.auth

data class UserRegisterResponse(
    val error: Boolean,
    val message: String
)

data class UserRegisterRequest(
    val name:String,
    val email: String,
    val password: String
)