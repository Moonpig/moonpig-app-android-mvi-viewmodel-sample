package com.moonpig.mvisample.productdetail

import com.moonpig.mvisample.domain.entities.ProductDetail
import com.moonpig.mvisample.domain.productdetail.ProductDetailAction
import com.moonpig.mvisample.domain.productdetail.ProductDetailResult
import com.moonpig.mvisample.domain.productdetail.ProductDetailUseCase
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ProductDetailViewModelTest {

    private val productDetailUseCase: ProductDetailUseCase = mock()
    private val productDetailTracker: ProductDetailTracker = mock()

    private val viewModel = givenAProductDetailViewModel()
    private val viewStateObserver = viewModel.viewState().test()

    @Test
    fun shouldOnlyUseOneInitialIntent() {
        whenever(productDetailUseCase.resultFrom(any()))
                .thenReturn(Observable.never())

        viewModel.bindIntents(Observable.merge(Observable.just(ProductDetailIntent.Initial(PRODUCT_ID)),
                                               Observable.just(ProductDetailIntent.Initial(PRODUCT_ID))))

        verify(productDetailUseCase, times(1)).resultFrom(any())
    }

    @Test
    fun shouldEmitNullObjectProductDetail_whenInitialised() {
        assertThat(viewStateObserver.values()[0].productDetail).isNull()
    }

    @Test
    fun shouldEmitInFlightState_whenGetProductInFlight() {
        whenever(productDetailUseCase.resultFrom(ProductDetailAction.LoadProductDetail(PRODUCT_ID)))
                .thenReturn(Observable.just(ProductDetailResult.GetProductDetail.InFlight))

        viewModel.bindIntents(Observable.just(ProductDetailIntent.Initial(PRODUCT_ID)))

        assertThat(viewStateObserver.values()[0].getProductDetailInFlight).isFalse()
        assertThat(viewStateObserver.values()[1].getProductDetailInFlight).isTrue()
    }

    @Test
    fun shouldEmitSuccessState_whenGetProductSuccess() {
        val productDetail = ProductDetailResult.GetProductDetail.Success(ProductDetail(NAME, DESCRIPTION, PRICE, IMAGE_URL))
        whenever(productDetailUseCase.resultFrom(ProductDetailAction.LoadProductDetail(PRODUCT_ID))).thenReturn(Observable.just(productDetail))

        viewModel.bindIntents(Observable.just(ProductDetailIntent.Initial(PRODUCT_ID)))

        val viewState = viewStateObserver.values()[1]
        assertThat(viewState.productDetail?.name).isEqualTo(NAME)
        assertThat(viewState.productDetail?.description).isEqualTo(DESCRIPTION)
        assertThat(viewState.productDetail?.price).isEqualTo("£$PRICE")
        assertThat(viewState.productDetail?.imageUrl).isEqualTo(IMAGE_URL)
        assertThat(viewState.getProductDetailInFlight).isFalse()
    }

    @Test
    fun shouldEmitErrorState_whenGetProductError() {
        val exception = RuntimeException()
        val error = ProductDetailResult.GetProductDetail.Error(exception)
        whenever(productDetailUseCase.resultFrom(ProductDetailAction.LoadProductDetail(PRODUCT_ID))).thenReturn(Observable.just(error))

        viewModel.bindIntents(Observable.just(ProductDetailIntent.Initial(PRODUCT_ID)))

        assertThat(viewStateObserver.values()[1].getProductDetailError).isEqualTo(exception)
        assertThat(viewStateObserver.values()[1].getProductDetailInFlight).isFalse()
    }

    @Test
    fun shouldEmitInFlightState_whenAddProductInFlight() {
        whenever(productDetailUseCase.resultFrom(ProductDetailAction.AddProductToBasket(PRODUCT_ID, QUANTITY)))
                .thenReturn(Observable.just(ProductDetailResult.AddProduct.InFlight))

        viewModel.bindIntents(Observable.just(ProductDetailIntent.AddToBasket(PRODUCT_ID, QUANTITY)))

        assertThat(viewStateObserver.values()[0].addToBasketInFlight).isFalse()
        assertThat(viewStateObserver.values()[1].addToBasketInFlight).isTrue()
    }

    @Test
    fun shouldEmitSuccessState_whenAddProductSuccess() {
        whenever(productDetailUseCase.resultFrom(ProductDetailAction.AddProductToBasket(PRODUCT_ID, QUANTITY)))
                .thenReturn(Observable.just(ProductDetailResult.AddProduct.InFlight, ProductDetailResult.AddProduct.Success))

        viewModel.bindIntents(Observable.just(ProductDetailIntent.AddToBasket(PRODUCT_ID, QUANTITY)))

        assertThat(viewStateObserver.values()[0].addToBasketInFlight).isFalse()
        assertThat(viewStateObserver.values()[1].addToBasketInFlight).isTrue()
        assertThat(viewStateObserver.values()[2].addToBasketInFlight).isFalse()
    }

    @Test
    fun shouldEmitErrorState_whenAddProductError() {
        val exception = RuntimeException()
        val error = ProductDetailResult.AddProduct.Error(exception)
        whenever(productDetailUseCase.resultFrom(ProductDetailAction.AddProductToBasket(PRODUCT_ID, QUANTITY))).thenReturn(Observable.just(error))

        viewModel.bindIntents(Observable.just(ProductDetailIntent.AddToBasket(PRODUCT_ID, QUANTITY)))

        assertThat(viewStateObserver.values()[0].addToBasketError).isNull()
        assertThat(viewStateObserver.values()[1].addToBasketError).isEqualTo(exception)
        assertThat(viewStateObserver.values()[1].addToBasketInFlight).isFalse()
    }

    private fun givenAProductDetailViewModel() = ProductDetailViewModel(productDetailUseCase, productDetailTracker)

    companion object {
        const val PRODUCT_ID = "122fkjkjfd"
        const val QUANTITY = 1
        const val DESCRIPTION = "description"
        const val NAME = "name"
        const val PRICE = 199
        const val IMAGE_URL = "imageUrl"
    }
}