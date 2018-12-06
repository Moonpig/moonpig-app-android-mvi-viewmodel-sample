package com.moonpig.mvisample.screentests.productdetail

import org.junit.Test

class ProductDetailTests {

    private val testRule = ProductDetailTestRule()

    private fun givenAProductDetailActivity() {
        testRule.launchActivity(null)
    }

    @Test
    fun shouldBeShowingLoadingIndicator() {
        givenAProductDetailActivity()

        ProductDetailRobot()
                .isLoading()
    }
}