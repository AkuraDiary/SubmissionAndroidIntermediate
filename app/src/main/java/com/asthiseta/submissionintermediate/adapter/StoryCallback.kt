package com.asthiseta.submissionintermediate.adapter

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.asthiseta.submissionintermediate.data.model.stories.Story

class StoryCallback(private val oldList: ArrayList<Story>, private val newList: ArrayList<Story>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }
    override fun areContentsTheSame(oldCourse: Int, newPosition: Int): Boolean {
        val (_, value, nameOld) = oldList[oldCourse]
        val (_, value1, nameNew) = newList[newPosition]
        return nameOld == nameNew && value == value1
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
