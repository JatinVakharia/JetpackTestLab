package com.practice.jetpack.testlab.schedular

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/* BaseSchedulerProvider used to provide schedulers to 'Live' app as well as to 'Testing' app
* This is the generic way to support Schedulers */

interface BaseSchedulerProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun ui(): Scheduler
}

class SchedulerProvider : BaseSchedulerProvider {
    override fun computation() = Schedulers.computation()
    override fun ui() = AndroidSchedulers.mainThread()
    override fun io() = Schedulers.io()
}

class TrampolineSchedulerProvider : BaseSchedulerProvider {
    override fun computation() = Schedulers.trampoline()
    override fun ui() = Schedulers.trampoline()
    override fun io() = Schedulers.trampoline()
}