package com.practice.jetpack.testlab.di

import com.practice.jetpack.testlab.api.APIService
import com.practice.jetpack.testlab.api.LoginService
import com.practice.jetpack.testlab.interceptor.LoginInterceptor
import com.practice.jetpack.testlab.repo.LoginRepo
import com.practice.jetpack.testlab.repo.LoginRepoImpl
import com.practice.jetpack.testlab.repo.StoriesRepo
import com.practice.jetpack.testlab.repo.StoriesRepoImpl
import com.practice.jetpack.testlab.schedular.BaseSchedulerProvider
import com.practice.jetpack.testlab.schedular.SchedulerProvider
import com.practice.jetpack.testlab.schedular.TrampolineSchedulerProvider
import com.practice.jetpack.testlab.ui.detail.DetailViewModel
import com.practice.jetpack.testlab.ui.login.LoginViewModel
import com.practice.jetpack.testlab.ui.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/* This injection module id using Koin library for dependency injection
* As this is a separate DI module, in future we can easily plug another DI module */

private val SERVER_URL = " https://hacker-news.firebaseio.com/v0/"
private val LOGIN_URL = " https://hacker-news.firebaseio.com/"

/* This module provides viewModel to live app*/
val uiModule = module {
    viewModel { LoginViewModel(get(), get("main")) }
    viewModel { MainViewModel(get(), get("main")) }
    viewModel { DetailViewModel() }
}

/* This module provides viewModel to testing app*/
val testUIModule = module {
    viewModel { LoginViewModel(get(), get("test")) }
    viewModel { MainViewModel(get(), get("test")) }
}

/* Scheduler module depending upon live and test app*/
val schedulerModule = module {
    single<BaseSchedulerProvider>("main") { SchedulerProvider() }
    single<BaseSchedulerProvider>("test") { TrampolineSchedulerProvider() }
}

/* Repository module to provide specific repository */
val repoModule = module {
    single<StoriesRepo> { StoriesRepoImpl(get()) }
    single<LoginRepo> { LoginRepoImpl(get()) }
}

/* API module to provide network related library objects */
val apiModule = module {

    single("main"){ createOkHttpClient() }

    single { createWebService<APIService>(get("main"), SERVER_URL) }

    single("login"){ createOkHttpClientLogin() }

    single { createWebService<LoginService>(get("login"), LOGIN_URL) }
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor).build()
}

/* OkHttpClient for Login, as it requires login mock response interceptor*/
fun createOkHttpClientLogin(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    val loginInterceptor = LoginInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
        .connectTimeout(10L, TimeUnit.SECONDS)
        .readTimeout(10L, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(loginInterceptor).build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}
