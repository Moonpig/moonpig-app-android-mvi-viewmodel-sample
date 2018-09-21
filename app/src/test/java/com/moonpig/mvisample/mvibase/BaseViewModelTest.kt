package com.moonpig.mvisample.mvibase

import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class BaseViewModelTest {

    @Test
    fun shouldBindInitialIntentAndReturnInitialViewState() {
        val testUseCase = TestUseCase()
        val testIntent = TestIntent.Test
        val testViewModel = TestViewModel(testUseCase)

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.just(testIntent))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.values().first()).isEqualTo(TestViewState.Idle())
        assertThat(testObserver.values()[1]).isEqualTo(TestViewState.Changed)
    }
}

sealed class TestIntent {
    object Test : TestIntent()
}

class TestUseCase : BaseUseCase<TestIntent, TestResult> {
    override fun processIntent(intent: TestIntent): TestResult {
        return TestResult.InitialResult
    }
}

class TestViewModel(testUseCase: BaseUseCase<TestIntent, TestResult>) :
        BaseViewModel<TestIntent, TestViewState, TestResult>(testUseCase) {

    override fun initialViewState(): TestViewState {
        return TestViewState.Idle()
    }
}

sealed class TestResult : BaseResult<TestViewState> {
    object InitialResult : TestResult() {
        override fun reduce(previousViewState: TestViewState): TestViewState {
            return TestViewState.Changed
        }
    }
}

sealed class TestViewState : BaseViewState {
    data class Idle(val testVal: Int = 4) : TestViewState()
    object Changed : TestViewState()
}