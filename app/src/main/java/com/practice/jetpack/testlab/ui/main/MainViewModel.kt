package com.practice.jetpack.testlab.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practice.jetpack.testlab.AbstractViewModel
import com.practice.jetpack.testlab.model.Story
import com.practice.jetpack.testlab.repo.StoriesRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Modifier
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(val storyRepo : StoriesRepo) : AbstractViewModel() {

    @Volatile var count = 0
    @Volatile var allStories = LinkedList<String>()
    @Volatile var stories = ArrayList<Story>()
    val storyLiveData = MutableLiveData<Story>()
    val allStoriesLiveData = MutableLiveData<Boolean>()

    fun getAllStories() : LiveData<Boolean>{
        if(allStories.isNullOrEmpty()) {
            launch {
                storyRepo.getAllStories()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        allStories.addAll(it)
                        allStoriesLiveData.value = true
                    }
            }
        }
        return allStoriesLiveData
    }

    fun getNext20() : LiveData<Story>{
        count = allStories.size - 20
        stories.clear()
        for (i in 0..19){
            storyRepo.getDataOfStory(allStories.get(i))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    print(it)
                    allStories.remove(it.id.toString())
                    storyLiveData.value = it
                }
        }
        return storyLiveData
    }
}
