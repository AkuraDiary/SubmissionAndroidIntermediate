package com.asthiseta.submissionintermediate.data.model.auth

data class UsrSession(
    val name: String,
    val token: String,
    val userId: String,
    val isLogin: Boolean
)