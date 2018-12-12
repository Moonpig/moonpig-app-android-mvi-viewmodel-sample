package com.moonpig.mvisample.domain

import com.moonpig.mvisample.domain.entities.ProductDetail
import com.moonpig.mvisample.domain.productdetail.AddProductRequest
import com.moonpig.mvisample.domain.productdetail.ProductDetailAction
import com.moonpig.mvisample.domain.productdetail.ProductDetailRepository
import com.moonpig.mvisample.domain.productdetail.ProductDetailResult
import com.moonpig.mvisample.domain.productdetail.ProductDetailUseCase
import com.moonpig.mvisample.domain.productdetail.RepositoryState
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Test

internal class ProductDetailUseCaseTest {

    private val productDetailRepository: ProductDetailRepository = mock()

    @Test
    fun shouldGetProductDetail() {
        whenever(productDetailRepository.getProductDetails(productId))
                .thenReturn(Observable.just(RepositoryState.GetProductDetail.InFlight,
                                            RepositoryState.GetProductDetail.Success(productDetail)))

        val productDetailUseCase = ProductDetailUseCase(productDetailRepository)

        val testObserver =
                productDetailUseCase.resultFrom(ProductDetailAction.LoadProductDetail(productId)).test()

        testObserver.assertValues(ProductDetailResult.GetProductDetail.InFlight,
                                  ProductDetailResult.GetProductDetail.Success(productDetail))
        testObserver.assertComplete()
    }

    @Test
    fun shouldReturnError_whenGetProductDetailFails() {
        whenever(productDetailRepository.getProductDetails(productId))
                .thenReturn(Observable.just(RepositoryState.GetProductDetail.InFlight,
                                            RepositoryState.GetProductDetail.Error(throwable)))

        val productDetailUseCase = ProductDetailUseCase(productDetailRepository)

        val testObserver =
                productDetailUseCase.resultFrom(ProductDetailAction.LoadProductDetail(productId)).test()

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
        val productDetail = ProductDetail(name = "name",
                                          description = "description",
                                          price = 199,
                                          imageUrl = "imageUrl")
        val throwable = Throwable()
        val productId = "1234FKF"
        val quantity = 1
        val addProductRequest = AddProductRequest(productId, quantity)
    }
}