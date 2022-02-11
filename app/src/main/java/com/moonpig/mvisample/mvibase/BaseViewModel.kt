package com.moonpig.mvisample.mvibase

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.moonpig.mvisample.domain.mvibase.BaseUseCase
import com.moonpig.mvisample.domain.mvibase.Maybe
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<
        Intention : Any,
        ViewAction : Any,
        Action : Any,
        Result : Any,
        ViewState : Any
        >(
    private val baseUseCase: BaseUseCase<Action, Result>,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val intentionSubject = PublishSubject.create<Intention>()
    private val viewActionSubject = BehaviorSubject.create<Maybe<ViewAction>>()
    private val stateSubject = BehaviorSubject.create<ViewState>()

    private lateinit var wiringDisposable: Disposable

    fun bind(
        initialIntention: Intention? = null,
        intentions: Observable<Intention>,
        handleViewState: (ViewState) -> Unit,
        handleViewAction: (ViewAction) -> Unit
    ): Disposable {

        TODO()
    }

    abstract fun initialViewState(): ViewState
    abstract fun actionFrom(intention: Intention): Either<ViewAction, Action>
    abstract fun reduce(previousViewState: ViewState, result: Result): ViewState
    open fun viewActionFrom(result: Result): ViewAction? = null

    protected fun action(action: Action) = Either.Action(action)
    protected fun viewAction(viewAction: ViewAction) = Either.ViewAction(viewAction)

    companion object {

        private const val viewStateKey = "viewState"
    }
}

sealed class Either<out ViewAction : Any, out Action : Any> {
    data class ViewAction<ViewAction : Any>(val value: ViewAction) : Either<ViewAction, Nothing>()
    data class Action<Action : Any>(val value: Action) : Either<Nothing, Action>()
}

