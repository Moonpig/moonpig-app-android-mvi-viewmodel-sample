package com.moonpig.mvisample.productdetail

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class ProductDetailRendererTest {

    private val view: ProductDetailView = mock()
    private val productDetailRenderer = ProductDetailRenderer()

    @Test
    fun shouldShowLoading_whenFetchingProductDetails() {
        val viewState = ProductDetailViewState(getProductDetailInFlight = true)

        productDetailRenderer.render(view, viewState)

        verify(view).isLoading(true)
    }

    @Test
    fun shouldHideLoading_whenFetchingProductDetailsFinished() {
        val viewState = ProductDetailViewState(getProductDetailInFlight = false)

        productDetailRenderer.render(view, viewState)

        verify(view).isLoading(false)
    }
}