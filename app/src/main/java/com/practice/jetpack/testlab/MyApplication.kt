package com.practice.jetpack.testlab

import android.app.Application
import com.practice.jetpack.testlab.di.apiModule
import com.practice.jetpack.testlab.di.repoModule
import com.practice.jetpack.testlab.di.schedulerModule
import com.practice.jetpack.testlab.di.uiModule
import org.koin.android.ext.android.startKoin


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(uiModule, schedulerModule, repoModule, apiModule))
    }
}