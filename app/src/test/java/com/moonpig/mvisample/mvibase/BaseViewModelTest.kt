package com.moonpig.mvisample.mvibase

import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class BaseViewModelTest {

    @Test
    fun shouldBindInitialIntentAndReturnInitialViewState() {
        val testUseCase = TestIntentProcessor()
        val testViewModel = TestViewModel(testUseCase)

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.just(TestIntent.First))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.values()[0]).isEqualTo(TestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(TestViewState.First)
    }

    @Test
    fun shouldNotEmitMultipleViewStates_whenNoChanges() {
        val testUseCase = TestIntentProcessor()
        val testViewModel = TestViewModel(testUseCase)

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.merge(Observable.just(TestIntent.First), Observable.just(TestIntent.First)))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.values().size).isEqualTo(2)
        assertThat(testObserver.values()[0]).isEqualTo(TestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(TestViewState.First)
    }

    @Test
    fun shouldEmitMultipleViewState_whenChanges() {
        val testUseCase = TestIntentProcessor()
        val testViewModel = TestViewModel(testUseCase)

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.merge(Observable.just(TestIntent.First), Observable.just(TestIntent.Second)))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.values().size).isEqualTo(3)
        assertThat(testObserver.values()[0]).isEqualTo(TestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(TestViewState.First)
        assertThat(testObserver.values()[2]).isEqualTo(TestViewState.Second)
    }
}

sealed class TestIntent {
    object First : TestIntent()
    object Second : TestIntent()
}

class TestIntentProcessor : BaseIntentProcessor<TestIntent, TestResult> {
    override fun resultFrom(intent: TestIntent): TestResult =
            when (intent) {
                TestIntent.First -> TestResult.First
                TestIntent.Second -> TestResult.Second
            }
}

class TestViewModel(testIntentProcessor: BaseIntentProcessor<TestIntent, TestResult>) :
        BaseViewModel<TestIntent, TestViewState, TestResult>(testIntentProcessor) {

    override fun initialViewState(): TestViewState {
        return TestViewState.Idle
    }
}

sealed class TestResult : BaseResult<TestViewState> {
    object First : TestResult() {
        override fun reduce(previousViewState: TestViewState): TestViewState {
            return TestViewState.First
        }
    }

    object Second : TestResult() {
        override fun reduce(previousViewState: TestViewState): TestViewState {
            return TestViewState.Second
        }
    }
}

sealed class TestViewState : BaseViewState {
    object Idle : TestViewState()
    object First : TestViewState()
    object Second : TestViewState()
}