package com.asthiseta.submissionintermediate.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asthiseta.submissionintermediate.data.model.stories.Story
import com.asthiseta.submissionintermediate.databinding.StoryLayoutBinding
import com.asthiseta.submissionintermediate.ui.activities.DetailActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.internal.managers.FragmentComponentManager

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.ViewHolder>(StoryCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  StoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: StoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(storyImage)

                tvStoryTitle.text = story.name
                tvStoryDesc.text = story.description

                storyLayoutRoot.setOnClickListener{
                    val intent = Intent(itemView.context, DetailActivity::class.java)

                    intent.apply {
                        putExtra("IMAGE", story.photoUrl)
                        putExtra("DESC", story.description)
                        putExtra("NAME", story.name)
                    }

                    val optionsCompat : ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        FragmentComponentManager.findActivity(itemView.context) as Activity,//(itemView.context as Activity),
                        Pair(storyImage, "photo"),
                        Pair(tvStoryTitle, "name"),
                        Pair(tvStoryDesc, "description")
                    )

                    (FragmentComponentManager.findActivity(itemView.context) as Activity).startActivity(intent, optionsCompat.toBundle())
                }
            }
        }

    }


    companion object{
        val StoryCallback = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}