package com.moonpig.mvisample.productdetail

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import java.io.IOException

class ProductDetailRendererTest {

    private val view: ProductDetailView = mock()
    private val productDetailRenderer = ProductDetailRenderer()

    @Test
    fun shouldHideErrorMessage_whenProductDetailErrorIsNull() {
        val viewState = ProductDetailScreenViewState (
                getProductDetailError = null
        )

        productDetailRenderer.render(view, viewState)

        verify(view).showLoadingError(false)
    }

    @Test
    fun shouldDisplayErrorMessage_whenProductDetailErrorSet() {
        val viewState = ProductDetailScreenViewState (
                getProductDetailError = IOException()
        )

        productDetailRenderer.render(view, viewState)

        verify(view).showLoadingError(true)
    }
}