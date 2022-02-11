package com.moonpig.mvisample.mvibase

import com.moonpig.mvisample.domain.mvibase.BaseAction
import com.moonpig.mvisample.domain.mvibase.BaseResult
import com.moonpig.mvisample.domain.mvibase.BaseUseCase
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class LegacyBaseViewModelTest {

    private val testUseCase: BaseUseCase<LegacyTestAction, LegacyTestResult> = mock()
    private val testTracker: LegacyBaseTracker<LegacyTestViewState, LegacyTestIntent> = mock()

    @Test
    fun shouldBindInitialIntentAndReturnInitialViewState() {
        val testViewModel = givenATestViewModel()

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.just(LegacyTestIntent.First))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.valueCount()).isEqualTo(2)
        assertThat(testObserver.values()[0]).isEqualTo(LegacyTestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(LegacyTestViewState.First)
    }

    @Test
    fun shouldNotEmitFirstIntentMoreThanOnce() {
        val testViewModel = givenATestViewModel()

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.merge(Observable.just(LegacyTestIntent.First),
                                                   Observable.just(LegacyTestIntent.Second),
                                                   Observable.just(LegacyTestIntent.First)))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.valueCount()).isEqualTo(3)
        assertThat(testObserver.values()[0]).isEqualTo(LegacyTestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(LegacyTestViewState.First)
        assertThat(testObserver.values()[2]).isEqualTo(LegacyTestViewState.Second)
    }

    @Test
    fun shouldNotEmitMultipleViewStates_whenNoChanges() {
        val testViewModel = givenATestViewModel()

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.merge(Observable.just(LegacyTestIntent.First), Observable.just(LegacyTestIntent.First)))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.valueCount()).isEqualTo(2)
        assertThat(testObserver.values()[0]).isEqualTo(LegacyTestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(LegacyTestViewState.First)
    }

    @Test
    fun shouldEmitMultipleViewState_whenChanges() {
        val testViewModel = givenATestViewModel()

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.merge(Observable.just(LegacyTestIntent.First), Observable.just(LegacyTestIntent.Second)))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.valueCount()).isEqualTo(3)
        assertThat(testObserver.values()[0]).isEqualTo(LegacyTestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(LegacyTestViewState.First)
        assertThat(testObserver.values()[2]).isEqualTo(LegacyTestViewState.Second)
    }

    @Test
    fun shouldTrackViewState_whenReduced() {
        val testViewModel = givenATestViewModel()

        testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.just(LegacyTestIntent.First))

        verify(testTracker).trackViewState(LegacyTestViewState.First)
    }

    @Test
    fun shouldTrackIntent_whenEmitted() {
        val testViewModel = givenATestViewModel()

        testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.just(LegacyTestIntent.First))

        verify(testTracker).trackIntent(LegacyTestIntent.First)
    }

    private fun givenATestViewModel(): LegacyTestViewModel {
        whenever(testUseCase.resultFrom(LegacyTestAction.First)).thenReturn(Observable.just(LegacyTestResult.First))
        whenever(testUseCase.resultFrom(LegacyTestAction.Second)).thenReturn(Observable.just(LegacyTestResult.Second))
        return LegacyTestViewModel(testUseCase, testTracker)
    }
}

class LegacyTestViewModel(testUseCase: BaseUseCase<LegacyTestAction, LegacyTestResult>,
                          testTracker: LegacyBaseTracker<LegacyTestViewState, LegacyTestIntent>) :
    LegacyBaseViewModel<LegacyTestIntent, LegacyTestAction, LegacyTestResult, LegacyTestViewState>(testUseCase, testTracker) {

    override fun intentFilter(): ObservableTransformer<LegacyTestIntent, LegacyTestIntent> =
            ObservableTransformer { observable ->
                observable.publish {
                    Observable.merge(it.ofType(LegacyTestIntent.First::class.java).take(1),
                                     it.filter { intent -> intent !is LegacyTestIntent.First })
                }
            }

    override fun actionFrom(intent: LegacyTestIntent): LegacyTestAction =
            when (intent) {
                LegacyTestIntent.First -> LegacyTestAction.First
                LegacyTestIntent.Second -> LegacyTestAction.Second
            }

    override fun initialViewState(): LegacyTestViewState = LegacyTestViewState.Idle

    override fun reduce(previousViewState: LegacyTestViewState, result: LegacyTestResult): LegacyTestViewState =
            when (result) {
                LegacyTestResult.First -> LegacyTestViewState.First
                LegacyTestResult.Second -> LegacyTestViewState.Second
            }
}

sealed class LegacyTestIntent : BaseIntent {
    object First : LegacyTestIntent()
    object Second : LegacyTestIntent()
}

sealed class LegacyTestAction : BaseAction {
    object First : LegacyTestAction()
    object Second : LegacyTestAction()
}

sealed class LegacyTestResult : BaseResult {
    object First : LegacyTestResult()
    object Second : LegacyTestResult()
}

sealed class LegacyTestViewState : BaseViewState {
    object Idle : LegacyTestViewState()
    object First : LegacyTestViewState()
    object Second : LegacyTestViewState()
}