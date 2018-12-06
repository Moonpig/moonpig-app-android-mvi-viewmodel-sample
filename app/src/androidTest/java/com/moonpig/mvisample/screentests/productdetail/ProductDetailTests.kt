package com.moonpig.mvisample.screentests.productdetail

import android.content.Intent
import com.moonpig.mvisample.domain.ProductDetailEntity
import com.moonpig.mvisample.domain.RepositoryState
import com.moonpig.mvisample.productdetail.ProductDetailActivity
import com.moonpig.mvisample.screentests.ScreenTestApplication
import com.moonpig.mvisample.screentests.di.DaggerTestApplicationComponent
import com.moonpig.mvisample.screentests.di.MockProductDetailDataModule
import com.nhaarman.mockito_kotlin.given
import io.reactivex.Observable
import org.junit.Rule
import org.junit.Test

class ProductDetailTests {

    @get:Rule
    val testRule = ProductDetailTestRule()

    private fun givenAProductDetailActivity() {
        ScreenTestApplication.overriddenApplicationComponent =
                DaggerTestApplicationComponent.builder()
                        .build()

        val intent = Intent(ScreenTestApplication.instance, ProductDetailActivity::class.java)
        testRule.launchActivity(intent)
    }

    @Test
    fun shouldBeShowingLoadingIndicator() {
        given(MockProductDetailDataModule.productDetailRepository.getProductDetails())
                .willReturn(Observable.just(RepositoryState.GetProductDetail.InFlight))
        givenAProductDetailActivity()

        ProductDetailRobot()
                .isLoading()
    }

    @Test
    fun shouldNotBeShowingLoadingIndicatorAfterFetchingFinished() {
        given(MockProductDetailDataModule.productDetailRepository.getProductDetails())
                .willReturn(Observable.just(
                        RepositoryState.GetProductDetail.InFlight,
                        RepositoryState.GetProductDetail.Success(ProductDetailEntity(
                                "name",
                                "desc",
                                0)
                        ))
                )
        givenAProductDetailActivity()

        ProductDetailRobot()
                .isNotLoading()
    }
}