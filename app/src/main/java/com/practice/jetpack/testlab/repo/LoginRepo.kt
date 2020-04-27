package com.practice.jetpack.testlab.repo

import com.practice.jetpack.androidtask.api.response.LoginResponse
import com.practice.jetpack.testlab.api.LoginService
import com.practice.jetpack.testlab.api.request.LoginRequest
import io.reactivex.Observable
import retrofit2.Response


interface LoginRepo{
    fun login(loginRequest: LoginRequest, url: String) : Observable<Response<LoginResponse>>
}

class LoginRepoImpl(private val loginService: LoginService) : LoginRepo{

    override fun login(
        loginRequest: LoginRequest,
        url: String
    ): Observable<Response<LoginResponse>> {
        return loginService.login(loginRequest, url).flatMapObservable {
            return@flatMapObservable Observable.just(it)
        }
    }
}