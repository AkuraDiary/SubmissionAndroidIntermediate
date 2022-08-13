package com.asthiseta.submissionintermediate.data.model.auth

data class Session(
    val name: String,
    val token: String,
    val userId: String,
    val isLogin: Boolean
)