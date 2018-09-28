package com.moonpig.mvisample.mvibase

import android.arch.lifecycle.ViewModel
import com.moonpig.mvisample.domain.mvibase.BaseAction
import com.moonpig.mvisample.domain.mvibase.BaseResult
import com.moonpig.mvisample.domain.mvibase.BaseUseCase
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<I : BaseIntent, A : BaseAction, VS : BaseViewState, R : BaseResult>(private val baseUseCase: BaseUseCase<A, R>) :
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
                    .map { actionFrom(it) }
                    .map { baseUseCase.resultFrom(it) }
                    .scan(initialViewState()) { previousViewState, result ->
                        reduce(previousViewState, result)
                    }
                    .distinctUntilChanged()
                    .subscribe(this)
        }
    }

    abstract fun initialViewState(): VS
    abstract fun actionFrom(intent: I): A
    abstract fun reduce(previousViewState: VS, result: R): VS
}

interface BaseViewState

interface BaseIntent