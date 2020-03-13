package com.practice.jetpack.testlab.api

import com.practice.jetpack.testlab.model.AllStories
import com.practice.jetpack.testlab.model.Story
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path


interface APIService {

    @GET("topstories.json")
    @Headers("Content-type: application/json")
    fun getAllStories() : Single<Array<String>>

    @GET("item/{storyId}.json")
    @Headers("Content-type: application/json")
    fun getDataOfStory(@Path("storyId") storyId: String) : Single<Story>

}