package com.moonpig.mvisample.mvibase

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<I, VS : BaseViewState, R : BaseResult<VS>>(private val baseIntentProcessor: BaseIntentProcessor<I, R>) :
        ViewModel() {

    private val intentSubject = PublishSubject.create<I>()
    private val viewStateObservable by lazy(::compose)

    fun bindIntents(intents: Observable<I>) {
        intents.subscribe(intentSubject)
    }

    fun viewState(): Observable<VS> {
        return viewStateObservable
    }

    private fun compose(): Observable<VS> {
        return BehaviorSubject.create<VS>().apply {
            intentSubject.map { baseIntentProcessor.resultFrom(it) }
                    .scan(initialViewState()) { previousViewState, result ->
                        result.reduce(previousViewState)
                    }
                    .distinctUntilChanged()
                    .subscribe(this)
        }
    }

    abstract fun initialViewState(): VS
}

interface BaseIntentProcessor<I, R> {
    fun resultFrom(intent: I): R
}

interface BaseResult<VS> {
    fun reduce(previousViewState: VS): VS
}

interface BaseViewState

