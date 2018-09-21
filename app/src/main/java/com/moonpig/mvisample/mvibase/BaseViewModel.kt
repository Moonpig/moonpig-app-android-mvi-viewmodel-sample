package com.moonpig.mvisample.mvibase

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<I, VS : BaseViewState, R : BaseResult<VS>>(private val baseUseCase: BaseUseCase<I, R>) :
        ViewModel() {

    private val intentSubject = PublishSubject.create<I>()
    private val viewStateObservable by lazy(::compose)
    private val reducer = BiFunction<VS, R, VS> { previousViewState, result ->
        result.reduce(previousViewState)
    }

    fun bindIntents(intents: Observable<I>) {
        intents.subscribe(intentSubject)
    }

    fun viewState(): Observable<VS> {
        return viewStateObservable
    }

    private fun compose(): Observable<VS> {
        return BehaviorSubject.create<VS>().apply {
            intentSubject.map { baseUseCase.processIntent(it) }
                    .scan(initialViewState(), reducer)
                    .distinctUntilChanged()
                    .subscribe(this)
        }
    }

    abstract fun initialViewState(): VS
}

interface BaseUseCase<I, R> {
    fun processIntent(intent: I): R
}

interface BaseResult<VS> {
    fun reduce(previousViewState: VS): VS
}

interface BaseViewState

