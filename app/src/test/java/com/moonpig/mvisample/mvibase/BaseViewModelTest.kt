package com.moonpig.mvisample.mvibase

import com.moonpig.mvisample.domain.mvibase.BaseAction
import com.moonpig.mvisample.domain.mvibase.BaseResult
import com.moonpig.mvisample.domain.mvibase.BaseUseCase
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class BaseViewModelTest {

    @Test
    fun shouldBindInitialIntentAndReturnInitialViewState() {
        val testUseCase = TestUseCase()
        val testViewModel = TestViewModel(testUseCase)

        val testObserver = testViewModel.viewState().test()
        testViewModel.bindIntents(Observable.just(TestIntent.First))

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.values()[0]).isEqualTo(TestViewState.Idle)
        assertThat(testObserver.values()[1]).isEqualTo(TestViewState.First)
    }

    @Test
    fun shouldNotEmitMultipleViewStates_whenNoChanges() {
        val testUseCase = TestUseCase()
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
        val testUseCase = TestUseCase()
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

class TestUseCase : BaseUseCase<TestAction, TestResult> {
    override fun resultFrom(action: TestAction): TestResult =
            when (action) {
                TestAction.First -> TestResult.First
                TestAction.Second -> TestResult.Second
            }
}

class TestViewModel(testUseCase: BaseUseCase<TestAction, TestResult>) :
        BaseViewModel<TestIntent, TestAction, TestViewState, TestResult>(testUseCase) {

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