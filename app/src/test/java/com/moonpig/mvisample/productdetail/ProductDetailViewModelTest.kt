package com.moonpig.mvisample.productdetail

import com.moonpig.mvisample.domain.ProductDetailAction
import com.moonpig.mvisample.domain.ProductDetailResult
import com.moonpig.mvisample.domain.ProductDetailUseCase
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ProductDetailViewModelTest {

    private val productDetailUseCase: ProductDetailUseCase = mock()

    @Test
    fun shouldEmitInFlightState_whenGetProductInFlight() {
        whenever(productDetailUseCase.resultFrom(ProductDetailAction.LoadProductDetail)).thenReturn(Observable.just(ProductDetailResult.GetProductDetail.InFlight))
        val viewModel = givenAProductDetailViewModel()
        val testObserver = viewModel.viewState().test()
        viewModel.bindIntents(Observable.just(ProductDetailIntent.Initial))

        assertThat(testObserver.values()[0].getProductDetailInFlight).isFalse()
        assertThat(testObserver.values()[1].getProductDetailInFlight).isTrue()
    }

    private val description = "description"
    private val name = "name"
    private val price = 199

    @Test
    fun shouldEmitSuccessState_whenGetProductSuccess() {
        val productDetail = ProductDetailResult.GetProductDetail.Success(name, description, price)
        whenever(productDetailUseCase.resultFrom(ProductDetailAction.LoadProductDetail)).thenReturn(Observable.just(productDetail))
        val viewModel = givenAProductDetailViewModel()
        val testObserver = viewModel.viewState().test()

        viewModel.bindIntents(Observable.just(ProductDetailIntent.Initial))

        assertThat(testObserver.values()[1].productDetail.name).isEqualTo(name)
        assertThat(testObserver.values()[1].productDetail.description).isEqualTo(description)
        assertThat(testObserver.values()[1].productDetail.price).isEqualTo(price)
        assertThat(testObserver.values()[1].getProductDetailInFlight).isFalse()
    }

    @Test
    fun shouldEmitErrorState_whenGetProductError() {
        val exception = RuntimeException()
        val error = ProductDetailResult.GetProductDetail.Error(exception)
        whenever(productDetailUseCase.resultFrom(ProductDetailAction.LoadProductDetail)).thenReturn(Observable.just(error))
        val viewModel = givenAProductDetailViewModel()
        val testObserver = viewModel.viewState().test()

        viewModel.bindIntents(Observable.just(ProductDetailIntent.Initial))

        assertThat(testObserver.values()[1].getProductDetailError).isEqualTo(exception)
        assertThat(testObserver.values()[1].getProductDetailInFlight).isFalse()
    }

    @Test
    fun shouldEmitInFlightState_whenAddProductInFlight() {
        val productId = "122fkjkjfd"
        val quantity = 1
        whenever(productDetailUseCase.resultFrom(ProductDetailAction.AddProductToBasket(productId, quantity))).thenReturn(Observable.just(ProductDetailResult.AddProduct.InFlight))
        val viewModel = givenAProductDetailViewModel()
        val testObserver = viewModel.viewState().test()
        viewModel.bindIntents(Observable.just(ProductDetailIntent.AddToBasket(productId, quantity)))

        assertThat(testObserver.values()[0].addToBasketInFlight).isFalse()
        assertThat(testObserver.values()[1].addToBasketInFlight).isTrue()
    }

    @Test
    fun shouldEmitSuccessState_whenAddProductSuccess() {
        val productId = "122fkjkjfd"
        val quantity = 1
        whenever(productDetailUseCase.resultFrom(ProductDetailAction.AddProductToBasket(productId, quantity))).thenReturn(Observable.just(ProductDetailResult.AddProduct.InFlight, ProductDetailResult.AddProduct.Success))
        val viewModel = givenAProductDetailViewModel()
        val testObserver = viewModel.viewState().test()
        viewModel.bindIntents(Observable.just(ProductDetailIntent.AddToBasket(productId, quantity)))

        assertThat(testObserver.values()[0].addToBasketInFlight).isFalse()
        assertThat(testObserver.values()[1].addToBasketInFlight).isTrue()
        assertThat(testObserver.values()[2].addToBasketInFlight).isFalse()
    }

    @Test
    fun shouldEmitErrorState_whenAddProductError() {
        val productId = "122fkjkjfd"
        val quantity = 1
        val exception = RuntimeException()
        val error = ProductDetailResult.AddProduct.Error(exception)
        whenever(productDetailUseCase.resultFrom(ProductDetailAction.AddProductToBasket(productId, quantity))).thenReturn(Observable.just(error))
        val viewModel = givenAProductDetailViewModel()
        val testObserver = viewModel.viewState().test()

        viewModel.bindIntents(Observable.just(ProductDetailIntent.AddToBasket(productId, quantity)))

        assertThat(testObserver.values()[0].addToBasketError).isNull()
        assertThat(testObserver.values()[1].addToBasketError).isEqualTo(exception)
        assertThat(testObserver.values()[1].addToBasketInFlight).isFalse()
    }

    private fun givenAProductDetailViewModel() = ProductDetailViewModel(productDetailUseCase)
}