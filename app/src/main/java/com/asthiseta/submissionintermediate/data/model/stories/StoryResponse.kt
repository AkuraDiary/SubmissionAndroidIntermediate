package com.asthiseta.submissionintermediate.data.model.stories

data class ResponseStory(
    val error: Boolean,
    val listStory: ArrayList<Story>,
    val message: String
)