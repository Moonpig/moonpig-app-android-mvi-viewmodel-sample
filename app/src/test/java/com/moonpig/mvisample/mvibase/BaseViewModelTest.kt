package com.moonpig.mvisample.mvibase

import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BaseViewModelTest {

    @Test
    internal fun shouldSubscribeIntents() {
        val testProcessor = TestProcessor()
        val baseViewModel = BaseViewModel(testProcessor)

        baseViewModel.processIntents(Observable.just(TestIntent.Test))
        val testObserver = baseViewModel.viewState().test()

        assertThat(testObserver.hasSubscription()).isTrue()
        assertThat(testObserver.values().single()).isInstanceOf(TestViewState::class.java)
    }
}

sealed class TestIntent {
    object Test : TestIntent()
}

data class TestViewState(val testVal: Int = 5)

class TestProcessor: BaseIntentProcessor<TestIntent, TestViewState> {
    override fun processIntent(intent: TestIntent): TestViewState {
        return TestViewState()
    }
}