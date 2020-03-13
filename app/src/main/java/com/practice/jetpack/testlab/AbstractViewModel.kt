package com.practice.jetpack.testlab

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class AbstractViewModel : ViewModel() {
    val disposable = CompositeDisposable()

    fun launch(job : () -> Disposable){
        disposable.add(job())
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}