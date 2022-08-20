package com.asthiseta.submissionintermediate.testUtilities

import com.asthiseta.submissionintermediate.data.model.stories.Story

object DummyData {
    fun generateDummyStoryResponseData() : List<Story>{
        val dummyStories : MutableList<Story> = arrayListOf()
        for (i in 1..70){
            dummyStories.add(
                Story(
                    "2022",
                    "Desc $i",
                    "ID $i",
                    "story_$i",
                    "someURL/image/$i",
                    i+42.069,
                    i+69.420
                )
            )
        }
        return dummyStories
    }
}