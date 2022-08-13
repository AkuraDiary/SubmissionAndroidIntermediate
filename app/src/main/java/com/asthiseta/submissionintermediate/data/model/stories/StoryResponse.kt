package com.asthiseta.submissionintermediate.data.model.stories

data class StoryListResponse(
    val error: Boolean,
    val listStory: ArrayList<Story>,
    val message: String
)
data class AddStoryResponse(
    val error: Boolean,
    val message: String
)