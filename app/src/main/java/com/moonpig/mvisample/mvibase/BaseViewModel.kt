package com.moonpig.mvisample.mvibase

import android.arch.lifecycle.ViewModel
import com.moonpig.mvisample.domain.mvibase.BaseAction
import com.moonpig.mvisample.domain.mvibase.BaseResult
import com.moonpig.mvisample.domain.mvibase.BaseUseCase
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<I : BaseIntent, A : BaseAction, R : BaseResult, VS : BaseViewState>(private val baseUseCase: BaseUseCase<A, R>,
                                                                                                 private val tracker: BaseTracker<VS, I>) :
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
            intentSubject
                    .doOnNext { tracker.trackIntent(it) }
                    .map { actionFrom(it) }
                    .flatMap { baseUseCase.resultFrom(it) }
                    .scan(initialViewState()) { previousViewState, result ->
                        reduce(previousViewState, result)
                    }
                    .distinctUntilChanged()
                    .doOnNext { tracker.trackViewState(it) }
                    .subscribe(this)
        }
    }

    protected abstract fun initialViewState(): VS
    protected abstract fun actionFrom(intent: I): A
    protected abstract fun reduce(previousViewState: VS, result: R): VS
}

interface BaseTracker<VS, I> {
    fun trackViewState(viewState: VS)
    fun trackIntent(intent: I)
}

interface BaseViewState

interface BaseIntent