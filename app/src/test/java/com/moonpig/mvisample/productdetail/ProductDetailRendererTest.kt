package com.moonpig.mvisample.productdetail

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
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

    @Test
    fun shouldDisplayProductDetails_whenFetchingSucceeded() {
        val viewState = ProductDetailViewState(
                productDetail = ProductDetail(
                        "Name",
                        "Description",
                        100,
                        "imageUrl"
                )
        )

        productDetailRenderer.render(view, viewState)

        verify(view).displayName("Name")
        verify(view).displayDescription("Description")
        verify(view).displayPrice("£100")
        verify(view).displayImage("imageUrl")
    }

    @Test
    fun shouldNotDisplayProductDetails_whenProductDetailIsNone() {
        val viewState = ProductDetailViewState(
                productDetail = ProductDetail.None
        )

        productDetailRenderer.render(view, viewState)

        verify(view, never()).displayName(any())
        verify(view, never()).displayDescription(any())
        verify(view, never()).displayPrice(any())
        verify(view, never()).displayImage(any())
    }
}