package com.example.mazadyapp.presentation.second

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mazadyapp.R
import com.example.mazadyapp.data.remote.model.Story
import com.example.mazadyapp.databinding.ItemStoryBinding

class StoriesAdapter : RecyclerView.Adapter<StoriesAdapter.StoryViewHolder>() {
    private var stories = listOf<Story>()

    class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {
            binding.apply {
                if (!story.isViewed) {
                    storyBorder.setImageResource(R.drawable.story_border_gradient)
                }
                storyImage.setImageResource(R.drawable.img_story)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount() = stories.size

    fun submitList(newStories: List<Story>) {
        stories = newStories
        notifyDataSetChanged()
    }
}