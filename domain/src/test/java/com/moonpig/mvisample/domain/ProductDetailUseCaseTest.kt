package com.moonpig.mvisample.domain

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Test

internal class ProductDetailUseCaseTest {

    private val productDetailRepository: ProductDetailRepository = mock()

    @Test
    fun shouldGetProductDetail() {
        whenever(productDetailRepository.getProductDetails())
                .thenReturn(Observable.just(RepositoryState.GetProductDetail.InFlight,
                                            RepositoryState.GetProductDetail.Success(PRODUCT_DETAIL)))

        val productDetailUseCase = ProductDetailUseCase(productDetailRepository)

        val testObserver = productDetailUseCase.getProductDetail().test()

        verify(productDetailRepository).getProductDetails()
        testObserver.assertValues(ProductDetailState.GetProductDetail.InFlight,
                                  ProductDetailState.GetProductDetail.Success(PRODUCT_DETAIL))
        testObserver.assertComplete()
    }

    @Test
    fun shouldReturnError_whenGetProductDetailFails() {
        whenever(productDetailRepository.getProductDetails())
                .thenReturn(Observable.just(RepositoryState.GetProductDetail.InFlight,
                                            RepositoryState.GetProductDetail.Error(throwable)))

        val productDetailUseCase = ProductDetailUseCase(productDetailRepository)

        val testObserver = productDetailUseCase.getProductDetail().test()

        verify(productDetailRepository).getProductDetails()
        testObserver.assertValues(ProductDetailState.GetProductDetail.InFlight,
                                  ProductDetailState.GetProductDetail.Error(throwable))
        testObserver.assertComplete()
    }

    @Test
    fun shouldAddProductToBasket() {
        whenever(productDetailRepository.addProductToBasket(PRODUCT_DETAIL))
                .thenReturn(Observable.just(RepositoryState.AddProduct.InFlight,
                                            RepositoryState.AddProduct.Success))
        val productDetailUseCase = ProductDetailUseCase(productDetailRepository)

        val testObserver = productDetailUseCase.addProductToBasket(PRODUCT_DETAIL).test()

        verify(productDetailRepository).addProductToBasket(PRODUCT_DETAIL)
        testObserver.assertValues(ProductDetailState.AddProduct.InFlight,
                                  ProductDetailState.AddProduct.Success)
        testObserver.assertComplete()
    }

    @Test
    fun shouldReturnError_whenAddProductToBasketFails() {
        whenever(productDetailRepository.addProductToBasket(PRODUCT_DETAIL))
                .thenReturn(Observable.just(RepositoryState.AddProduct.InFlight,
                                            RepositoryState.AddProduct.Error(throwable)))
        val productDetailUseCase = ProductDetailUseCase(productDetailRepository)

        val testObserver = productDetailUseCase.addProductToBasket(PRODUCT_DETAIL).test()

        verify(productDetailRepository).addProductToBasket(PRODUCT_DETAIL)
        testObserver.assertValues(ProductDetailState.AddProduct.InFlight,
                                  ProductDetailState.AddProduct.Error(throwable))
        testObserver.assertComplete()
    }

    companion object {
        val PRODUCT_DETAIL = ProductDetailEntity(name = "name",
                                                 description = "description",
                                                 price = 199)
        val throwable = Throwable()
    }
}