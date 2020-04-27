package com.practice.jetpack.testlab.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practice.jetpack.testlab.AbstractViewModel
import com.practice.jetpack.testlab.model.Story
import com.practice.jetpack.testlab.repo.StoriesRepo
import com.practice.jetpack.testlab.schedular.BaseSchedulerProvider
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(private val storyRepo : StoriesRepo,
                    private val schedulerProvider: BaseSchedulerProvider
) : AbstractViewModel() {

    @Volatile var count = 0
    @Volatile var allStories = LinkedList<String>()
    @Volatile var stories = ArrayList<Story>()
    private val loadStoryCount = 20
    val storyLiveData = MutableLiveData<Story>()
    val allStoriesLiveData = MutableLiveData<Boolean>()

    // Get All Story Count
    fun getAllStories() : LiveData<Boolean>{
        if(allStories.isNullOrEmpty()) {
            launch {
                storyRepo.getAllStories()
                    .observeOn(schedulerProvider.ui())
                    .subscribeOn(schedulerProvider.io())
                    .subscribe {
                        allStories.addAll(it)
                        allStoriesLiveData.value = true
                    }
            }
        }
        return allStoriesLiveData
    }

    fun refreshAllStories(){
        // Clear Story ID list
        allStories.clear()
        // Get All Stories
        getAllStories()
    }

    // Load Stories when required
    fun loadMoreStories(){
        getNextStories()
    }

    // Load next set of Stories at a time
    fun getNextStories() : LiveData<Story>{
        if(allStories.isNotEmpty()) {
            count = allStories.size - loadStoryCount
            stories.clear()
            for (i in 0 until loadStoryCount) {
                getStory(allStories[i])
            }
        }
        return storyLiveData
    }

    // Load Single Story
    fun getStory(itemId : String){
        launch {
            storyRepo.getDataOfStory(itemId)
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .subscribe {
                    allStories.remove(it.id.toString())
                    storyLiveData.value = it
                }
        }
    }
}
