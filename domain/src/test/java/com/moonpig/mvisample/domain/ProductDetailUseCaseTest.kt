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
                                            RepositoryState.GetProductDetail.Success(productDetailEntity)))

        val productDetailUseCase = ProductDetailUseCase(productDetailRepository)

        val testObserver =
                productDetailUseCase.resultFrom(ProductDetailAction.LoadProductDetail).test()

        verify(productDetailRepository).getProductDetails()
        testObserver.assertValues(ProductDetailResult.GetProductDetail.InFlight,
                                  ProductDetailResult.GetProductDetail.Success(productDetailEntity.name,
                                                                               productDetailEntity.description,
                                                                               productDetailEntity.price))
        testObserver.assertComplete()
    }

    @Test
    fun shouldReturnError_whenGetProductDetailFails() {
        whenever(productDetailRepository.getProductDetails())
                .thenReturn(Observable.just(RepositoryState.GetProductDetail.InFlight,
                                            RepositoryState.GetProductDetail.Error(throwable)))

        val productDetailUseCase = ProductDetailUseCase(productDetailRepository)

        val testObserver =
                productDetailUseCase.resultFrom(ProductDetailAction.LoadProductDetail).test()

        verify(productDetailRepository).getProductDetails()
        testObserver.assertValues(ProductDetailResult.GetProductDetail.InFlight,
                                  ProductDetailResult.GetProductDetail.Error(throwable))
        testObserver.assertComplete()
    }

    @Test
    fun shouldAddProductToBasket() {
        whenever(productDetailRepository.addProductToBasket(addProductRequest))
                .thenReturn(Observable.just(RepositoryState.AddProduct.InFlight,
                                            RepositoryState.AddProduct.Success))
        val productDetailUseCase = ProductDetailUseCase(productDetailRepository)

        val testObserver = productDetailUseCase
                .resultFrom(ProductDetailAction.AddProductToBasket(productId, quantity)).test()

        verify(productDetailRepository).addProductToBasket(addProductRequest)
        testObserver.assertValues(ProductDetailResult.AddProduct.InFlight,
                                  ProductDetailResult.AddProduct.Success)
        testObserver.assertComplete()
    }

    @Test
    fun shouldReturnError_whenAddProductToBasketFails() {
        whenever(productDetailRepository.addProductToBasket(addProductRequest))
                .thenReturn(Observable.just(RepositoryState.AddProduct.InFlight,
                                            RepositoryState.AddProduct.Error(throwable)))
        val productDetailUseCase = ProductDetailUseCase(productDetailRepository)

        val testObserver = productDetailUseCase
                .resultFrom(ProductDetailAction.AddProductToBasket(productId, quantity)).test()

        verify(productDetailRepository).addProductToBasket(addProductRequest)
        testObserver.assertValues(ProductDetailResult.AddProduct.InFlight,
                                  ProductDetailResult.AddProduct.Error(throwable))
        testObserver.assertComplete()
    }

    companion object {
        val productDetailEntity = ProductDetailEntity(name = "name",
                                                      description = "description",
                                                      price = 199)
        val throwable = Throwable()
        val productId = "1234FKF"
        val quantity = 1
        val addProductRequest = AddProductRequest(productId, quantity)
    }
}