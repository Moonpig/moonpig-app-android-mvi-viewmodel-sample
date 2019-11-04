package com.moonpig.mvisample.mvibase

import android.arch.lifecycle.ViewModel
import com.moonpig.mvisample.domain.mvibase.BaseAction
import com.moonpig.mvisample.domain.mvibase.BaseResult
import com.moonpig.mvisample.domain.mvibase.BaseUseCase
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<
        I : BaseIntent,
        VA : BaseViewAction,
        A : BaseAction,
        R : BaseResult,
        VS : BaseViewState
        >(
    private val baseUseCase: BaseUseCase<A, R>,
    private val tracker: BaseTracker<VS, I, VA>
) : ViewModel() {

    private val intentSubject = PublishSubject.create<I>()
    private val viewStateObservable by lazy(::compose)
    private val viewActionSubject = BehaviorSubject.create<Maybe<VA>>()

    fun bindIntents(intents: Observable<I>) {
        intents.subscribe(intentSubject)
    }

    fun viewState(): Observable<VS> {
        return viewStateObservable
    }

    /**
     * Ensures that a ViewAction is emitted exactly once.
     * The [BehaviorSubject] guarantees that we remember the last value if currently there are no
     * subscribers.
     * The [Maybe] in combination with the [BehaviorSubject.doAfterNext] "clears" the cached value
     * on emission. This way of "clearing" the cache is suggested in the [BehaviorSubject]
     * documentation.
     */
    fun viewAction(): Observable<VA> = viewActionSubject
        .flatMap { if (it is Maybe.Just) Observable.just(it.viewAction) else Observable.empty() }
        .doAfterNext { viewActionSubject.onNext(Maybe.Nothing()) }

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
            .flatMap { either ->
                when (either) {
                    is Either.ViewAction ->
                        Observable.empty<R>().also { emitViewAction(either.value) }
                    is Either.Action ->
                        baseUseCase.resultFrom(either.value)
                }
            }
            .scan(initialViewState(), ::reduce)
            .distinctUntilChanged()
            .doOnNext(tracker::trackViewState)
            .replay(1)
            .autoConnect()

    private fun emitViewAction(viewAction: VA) {
        tracker.trackViewAction(viewAction)
        viewActionSubject.onNext(Maybe.Just(viewAction))
    }

    protected abstract fun intentFilter(): ObservableTransformer<I, I>
    protected abstract fun initialViewState(): VS
    protected abstract fun actionFrom(intent: I): Either<VA, A>
    protected open fun viewActionFrom(result: R): VA? = null
    protected abstract fun reduce(previousViewState: VS, result: R): VS

    protected fun action(action: A) = Either.Action<VA, A>(action)
    protected fun viewAction(viewAction: VA) = Either.ViewAction<VA, A>(viewAction)
}

interface BaseTracker<VS, I, VA> {
    fun trackIntent(intent: I)
    fun trackViewAction(viewAction: VA)
    fun trackViewState(viewState: VS)
}

interface BaseViewAction

interface BaseViewState

interface BaseIntent

sealed class Either<VA, A> {
    data class ViewAction<VA : BaseViewAction, A : BaseAction>(val value: VA) : Either<VA, A>()
    data class Action<VA : BaseViewAction, A : BaseAction>(val value: A) : Either<VA, A>()
}

sealed class Maybe<VA> {
    class Nothing<VA : BaseViewAction> : Maybe<VA>()
    data class Just<VA : BaseViewAction>(val viewAction: VA) : Maybe<VA>()
}