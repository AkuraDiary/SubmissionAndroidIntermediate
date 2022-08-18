package com.asthiseta.submissionintermediate.data.model.auth

data class UsrSession(
    val userId: String,
    val name: String,
    val token: String,
    val isLogin: Boolean
)