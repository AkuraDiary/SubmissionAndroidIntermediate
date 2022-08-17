package com.asthiseta.submissionintermediate.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asthiseta.submissionintermediate.data.model.stories.Story
import com.asthiseta.submissionintermediate.databinding.StoryLayoutBinding
import com.asthiseta.submissionintermediate.ui.activities.DetailActivity
import com.bumptech.glide.Glide

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    private val listStoryData = ArrayList<Story>()
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
                        itemView.context as Activity,
                        Pair(storyImage, "photo"),
                        Pair(tvStoryTitle, "name"),
                        Pair(tvStoryDesc, "description")
                    )

                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  StoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStoryData[position])
    }

    override fun getItemCount(): Int {
        return listStoryData.size
    }

    fun setStoryData(story : ArrayList<Story>){
        val diffCallback = StoryCallback(listStoryData, story)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listStoryData.clear()
        listStoryData.addAll(story)
        diffResult.dispatchUpdatesTo(this)

    }
}