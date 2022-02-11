package com.moonpig.mvisample.mvibase

import android.arch.lifecycle.ViewModel
import com.moonpig.mvisample.domain.mvibase.BaseAction
import com.moonpig.mvisample.domain.mvibase.BaseResult
import com.moonpig.mvisample.domain.mvibase.BaseUseCase
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

abstract class LegacyBaseViewModel<I : BaseIntent, A : BaseAction, R : BaseResult, VS : BaseViewState>(private val baseUseCase: BaseUseCase<A, R>,
                                                                                                       private val tracker: LegacyBaseTracker<VS, I>) :
        ViewModel() {

    private val intentSubject = PublishSubject.create<I>()
    private val viewStateObservable by lazy(::compose)

    fun bindIntents(intents: Observable<I>) {
        intents.subscribe(intentSubject)
    }

    fun viewState(): Observable<VS> {
        return viewStateObservable
    }

    /**
     * We use `replay(1).autoConnect()` instead of a [io.reactivex.subjects.BehaviorSubject] to
     * cache the last item. This is important as the BehaviorSubject will clear the last item if
     * it is terminated. This might happen on simple views that just render out data and don't have
     * any intent sources that stay open throughout the activity's lifetime.
     */
    private fun compose(): Observable<VS> =
            intentSubject
                    .compose(intentFilter())
                    .doOnNext(tracker::trackIntent)
                    .map(::actionFrom)
                    .flatMap(baseUseCase::resultFrom)
                    .scan(initialViewState(), ::reduce)
                    .distinctUntilChanged()
                    .doOnNext(tracker::trackViewState)
                    .replay(1)
                    .autoConnect()

    protected abstract fun intentFilter(): ObservableTransformer<I, I>
    protected abstract fun initialViewState(): VS
    protected abstract fun actionFrom(intent: I): A
    protected abstract fun reduce(previousViewState: VS, result: R): VS
}

interface LegacyBaseTracker<VS, I> {
    fun trackViewState(viewState: VS)
    fun trackIntent(intent: I)
}

interface BaseViewState

interface BaseIntent