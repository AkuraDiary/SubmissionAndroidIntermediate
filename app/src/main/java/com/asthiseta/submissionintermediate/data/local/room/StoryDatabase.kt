package com.asthiseta.submissionintermediate.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.asthiseta.submissionintermediate.data.local.entity.RemoteKeyEntity
import com.asthiseta.submissionintermediate.data.model.stories.Story


@Database(entities = [Story::class, RemoteKeyEntity::class], version = 3, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}