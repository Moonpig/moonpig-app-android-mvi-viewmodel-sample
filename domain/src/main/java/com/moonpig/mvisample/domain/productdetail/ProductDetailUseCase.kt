package com.moonpig.mvisample.domain.productdetail

import com.moonpig.mvisample.domain.mvibase.BaseUseCase
import io.reactivex.Observable

class ProductDetailUseCase(private val productDetailRepository: ProductDetailRepository) :
        BaseUseCase<ProductDetailAction, ProductDetailResult> {

    override fun resultFrom(action: ProductDetailAction): Observable<ProductDetailResult> {
        return when (action) {
            is ProductDetailAction.LoadProductDetail -> getProductDetail()
            is ProductDetailAction.AddProductToBasket -> addProductToBasket(action.productId, action.quantity)
        }
    }

    private fun getProductDetail(): Observable<ProductDetailResult> =
            productDetailRepository.getProductDetails()
                    .map {
                        when (it) {
                            is RepositoryState.GetProductDetail.InFlight -> ProductDetailResult.GetProductDetail.InFlight
                            is RepositoryState.GetProductDetail.Success -> ProductDetailResult.GetProductDetail.Success(it.productDetail)
                            is RepositoryState.GetProductDetail.Error -> ProductDetailResult.GetProductDetail.Error(it.throwable)
                        }
                    }

    private fun addProductToBasket(productId: String, quantity: Int): Observable<ProductDetailResult> =
            productDetailRepository.addProductToBasket(AddProductRequest(productId, quantity))
                    .map {
                        when (it) {
                            is RepositoryState.AddProduct.InFlight -> ProductDetailResult.AddProduct.InFlight
                            is RepositoryState.AddProduct.Success -> ProductDetailResult.AddProduct.Success
                            is RepositoryState.AddProduct.Error -> ProductDetailResult.AddProduct.Error(it.throwable)
                        }
                    }
}