package com.moonpig.mvisample.mvibase

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.moonpig.mvisample.domain.mvibase.BaseResult
import com.moonpig.mvisample.domain.mvibase.BaseUseCase
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize
import org.junit.Test

internal class BaseViewModelTest {

    private val testUseCase: BaseUseCase<TestAction, TestResult> = mock()
    private val savedStateHandle = SavedStateHandle()
    private val testViewModel = TestViewModel(testUseCase, savedStateHandle)

    @Test
    fun `WHEN intent reduced to new view state THEN new view state emitted`() {
        val handleViewState = mockViewStateHandler()

        testViewModel.bind(
            intentions = Observable.just(TestIntention.Regular),
            handleViewState = handleViewState,
            handleViewAction = {}
        ).dispose()

        inOrder(handleViewState) {
            verify(handleViewState).invoke(initialState)
            verify(handleViewState).invoke(modifiedState)
        }
    }

    private fun mockViewStateHandler() = mock<(TestViewState) -> Unit> {
        on { invoke(any()) } doReturn Unit
    }

    companion object {

        val initialState = TestViewState(0)
        val modifiedState = TestViewState(1)
    }

    class TestViewModel(
        testUseCase: BaseUseCase<TestAction, TestResult>,
        savedStateHandle: SavedStateHandle
    ) :
        BaseViewModel<TestIntention, TestViewAction, TestAction, TestResult, TestViewState>(
            testUseCase,
            savedStateHandle
        ) {

        override fun initialViewState(): TestViewState {
            TODO()
        }

        override fun actionFrom(intention: TestIntention): Either<TestViewAction, TestAction> {
            TODO()
        }

        override fun reduce(previousViewState: TestViewState, result: TestResult): TestViewState {
            TODO()
        }
    }

    sealed class TestIntention {
        object NoChange : TestIntention()
        object Regular : TestIntention()
    }

    sealed class TestAction {
        object NoChange : TestAction()
        object Regular : TestAction()
    }

    sealed class TestViewAction {

    }

    sealed class TestResult : BaseResult {
        object NoChange : TestResult()
        object Regular : TestResult()
    }


    @Parcelize
    data class TestViewState(val count: Int) : Parcelable
}
