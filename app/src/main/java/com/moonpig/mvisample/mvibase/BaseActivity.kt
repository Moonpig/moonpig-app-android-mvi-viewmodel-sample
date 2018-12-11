package com.moonpig.mvisample.mvibase

import android.annotation.SuppressLint
import android.app.Activity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

@SuppressLint("Registered")
open class BaseActivity: Activity() {

    protected val disposables = CompositeDisposable()

    protected fun Disposable.addToDisposables() =
            disposables.add(this)
}