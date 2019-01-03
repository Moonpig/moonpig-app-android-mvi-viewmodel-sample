package com.moonpig.mvisample.mvibase

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    protected val disposables = CompositeDisposable()

    protected fun Disposable.addToDisposables() =
            disposables.add(this)
}