package com.practice.jetpack.testlab.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.practice.jetpack.testlab.R
import com.practice.jetpack.testlab.model.Story
import kotlinx.android.synthetic.main.item_story.view.*
import kotlin.collections.ArrayList

class StoriesListAdapter(val context : Context) : RecyclerView.Adapter<StoriesItemViewHolder>(){

    private lateinit var storyList : MutableList<Story>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesItemViewHolder {
        return StoriesItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_story, parent, false))
    }

    fun setMyStoryList(stories : ArrayList<Story>){
        storyList = stories
        notifyDataSetChanged()
    }

    fun addMyStories(stories : List<Story>){
        val size = this.storyList.size
        this.storyList.addAll(stories)
        val sizeNew = this.storyList.size
        notifyItemRangeChanged(size, sizeNew)
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    override fun onBindViewHolder(holder: StoriesItemViewHolder, position: Int) {
        holder.tvTitle.text = storyList.get(index = position).title
        holder.tvType.text = storyList.get(index = position).type
        holder.tvStoryUrl.text = storyList.get(index = position).url

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(storyList.get(index = position).url, storyList.get(index = position).title))
        }
    }
}

class StoriesItemViewHolder(view : View ):RecyclerView.ViewHolder(view){
    val tvTitle: TextView = view.tv_title
    val tvType: TextView = view.tv_type
    val tvStoryUrl: TextView = view.tv_url
}