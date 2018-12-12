package com.moonpig.mvisample.productdetail

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import java.io.IOException

class ProductDetailRendererTest {

    private val view: ProductDetailView = mock()
    private val productDetailRenderer = ProductDetailRenderer()

    @Test
    fun shouldShowLoading_whenFetchingProductDetails() {
        val viewState = ProductDetailScreenViewState(getProductDetailInFlight = true)

        productDetailRenderer.render(view, viewState)

        verify(view).isLoading(true)
    }

    @Test
    fun shouldHideLoading_whenFetchingProductDetailsFinished() {
        val viewState = ProductDetailScreenViewState(getProductDetailInFlight = false)

        productDetailRenderer.render(view, viewState)

        verify(view).isLoading(false)
    }

    @Test
    fun shouldDisplayProductDetails_whenFetchingSucceeded() {
        val viewState = ProductDetailScreenViewState(
                productDetail = ProductDetailViewState(
                        "Name",
                        "Description",
                        100,
                        "imageUrl"
                )
        )

        productDetailRenderer.render(view, viewState)

        verify(view).displayPrice("£100")
    }

    @Test
    fun shouldNotDisplayProductDetails_whenProductDetailIsNone() {
        val viewState = ProductDetailScreenViewState(
                productDetail = null
        )

        productDetailRenderer.render(view, viewState)

        verify(view, never()).displayPrice(any())
      }

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