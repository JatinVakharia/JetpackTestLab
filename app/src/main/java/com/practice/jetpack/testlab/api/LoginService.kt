package com.practice.jetpack.testlab.api

import com.practice.jetpack.androidtask.api.response.LoginResponse
import com.practice.jetpack.testlab.api.request.LoginRequest
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path


interface LoginService {

    @POST("{url}")
    @Headers("Content-type: application/json")
    fun login(@Body loginRequest: LoginRequest, @Path("url") url: String) : Single<Response<LoginResponse>>

}