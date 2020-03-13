package com.practice.jetpack.testlab.repo

import com.practice.jetpack.testlab.api.APIService
import com.practice.jetpack.testlab.model.AllStories
import com.practice.jetpack.testlab.model.Story
import io.reactivex.Observable


interface StoriesRepo{
    fun getAllStories() : Observable<Array<String>>

    fun getDataOfStory(tag : String) : Observable<Story>
}

class StoriesRepoImpl(val apiService: APIService) : StoriesRepo{
    override fun getAllStories(): Observable<Array<String>> {
        return apiService.getAllStories().flatMapObservable {
            return@flatMapObservable Observable.just(it)
        }
    }

    override fun getDataOfStory(storyId: String): Observable<Story> {
        return apiService.getDataOfStory(storyId = storyId).flatMapObservable {
            return@flatMapObservable Observable.just(it)
        }
    }
}