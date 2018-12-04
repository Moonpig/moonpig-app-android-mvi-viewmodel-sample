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

internal class BaseViewModelTest {

    private val testUseCase: BaseUseCase<TestAction, TestResult> = mock()
    private val testTracker: BaseTracker<TestViewState, TestIntent> = mock()

    @Test
    fun shouldBindInitialIntentAndReturnInitialViewState() {
        val testViewModel = givenATestViewModel()

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.just(TestIntent.First))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.valueCount()).isEqualTo(2)
        assertThat(testObserver.values()[0]).isEqualTo(TestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(TestViewState.First)
    }

    @Test
    fun shouldNotEmitFirstIntentMoreThanOnce() {
        val testViewModel = givenATestViewModel()

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.merge(Observable.just(TestIntent.First),
                                                   Observable.just(TestIntent.Second),
                                                   Observable.just(TestIntent.First)))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.valueCount()).isEqualTo(3)
        assertThat(testObserver.values()[0]).isEqualTo(TestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(TestViewState.First)
        assertThat(testObserver.values()[2]).isEqualTo(TestViewState.Second)
    }

    @Test
    fun shouldNotEmitMultipleViewStates_whenNoChanges() {
        val testViewModel = givenATestViewModel()

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.merge(Observable.just(TestIntent.First), Observable.just(TestIntent.First)))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.valueCount()).isEqualTo(2)
        assertThat(testObserver.values()[0]).isEqualTo(TestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(TestViewState.First)
    }

    @Test
    fun shouldEmitMultipleViewState_whenChanges() {
        val testViewModel = givenATestViewModel()

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.merge(Observable.just(TestIntent.First), Observable.just(TestIntent.Second)))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.valueCount()).isEqualTo(3)
        assertThat(testObserver.values()[0]).isEqualTo(TestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(TestViewState.First)
        assertThat(testObserver.values()[2]).isEqualTo(TestViewState.Second)
    }

    @Test
    fun shouldTrackViewState_whenReduced() {
        val testViewModel = givenATestViewModel()

        testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.just(TestIntent.First))

        verify(testTracker).trackViewState(TestViewState.First)
    }

    @Test
    fun shouldTrackIntent_whenEmitted() {
        val testViewModel = givenATestViewModel()

        testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.just(TestIntent.First))

        verify(testTracker).trackIntent(TestIntent.First)
    }

    private fun givenATestViewModel(): TestViewModel {
        whenever(testUseCase.resultFrom(TestAction.First)).thenReturn(Observable.just(TestResult.First))
        whenever(testUseCase.resultFrom(TestAction.Second)).thenReturn(Observable.just(TestResult.Second))
        return TestViewModel(testUseCase, testTracker)
    }
}

class TestViewModel(testUseCase: BaseUseCase<TestAction, TestResult>,
                    testTracker: BaseTracker<TestViewState, TestIntent>) :
        BaseViewModel<TestIntent, TestAction, TestResult, TestViewState>(testUseCase, testTracker) {

    override fun intentFilter(): ObservableTransformer<TestIntent, TestIntent> =
            ObservableTransformer { observable ->
                observable.publish {
                    Observable.merge(it.ofType(TestIntent.First::class.java).take(1),
                                     it.filter { intent -> intent !is TestIntent.First })
                }
            }

    override fun actionFrom(intent: TestIntent): TestAction =
            when (intent) {
                TestIntent.First -> TestAction.First
                TestIntent.Second -> TestAction.Second
            }

    override fun initialViewState(): TestViewState = TestViewState.Idle

    override fun reduce(previousViewState: TestViewState, result: TestResult): TestViewState =
            when (result) {
                TestResult.First -> TestViewState.First
                TestResult.Second -> TestViewState.Second
            }
}

sealed class TestIntent : BaseIntent {
    object First : TestIntent()
    object Second : TestIntent()
}

sealed class TestAction : BaseAction {
    object First : TestAction()
    object Second : TestAction()
}

sealed class TestResult : BaseResult {
    object First : TestResult()
    object Second : TestResult()
}

sealed class TestViewState : BaseViewState {
    object Idle : TestViewState()
    object First : TestViewState()
    object Second : TestViewState()
}