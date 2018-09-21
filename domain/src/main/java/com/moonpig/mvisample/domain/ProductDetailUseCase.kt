package com.moonpig.mvisample.domain

import io.reactivex.Observable

class ProductDetailUseCase(private val productDetailRepository: ProductDetailRepository) {
    fun getProductDetail(): Observable<ProductDetailState> =
            productDetailRepository.getProductDetails()
                    .map {
                        when (it) {
                            is RepositoryState.GetProductDetail.InFlight -> ProductDetailState.GetProductDetail.InFlight
                            is RepositoryState.GetProductDetail.Success -> ProductDetailState.GetProductDetail.Success(it.productDetailEntity)
                            is RepositoryState.GetProductDetail.Error -> ProductDetailState.GetProductDetail.Error(it.throwable)
                        }
                    }

    fun addProductToBasket(productDetailEntity: ProductDetailEntity): Observable<ProductDetailState.AddProduct> =
            productDetailRepository.addProductToBasket(productDetailEntity)
                    .map {
                        when (it) {
                            is RepositoryState.AddProduct.InFlight -> ProductDetailState.AddProduct.InFlight
                            is RepositoryState.AddProduct.Success -> ProductDetailState.AddProduct.Success
                            is RepositoryState.AddProduct.Error -> ProductDetailState.AddProduct.Error(it.throwable)
                        }
                    }
}

data class ProductDetailEntity(val name: String,
                               val description: String,
                               val price: Int)

interface ProductDetailRepository {
    fun getProductDetails(): Observable<RepositoryState.GetProductDetail>
    fun addProductToBasket(productDetailEntity: ProductDetailEntity): Observable<RepositoryState.AddProduct>
}

sealed class RepositoryState {
    sealed class GetProductDetail : RepositoryState() {
        object InFlight : GetProductDetail()
        data class Success(val productDetailEntity: ProductDetailEntity) : GetProductDetail()
        data class Error(val throwable: Throwable) : GetProductDetail()
    }

    sealed class AddProduct : RepositoryState() {
        object InFlight : AddProduct()
        object Success : AddProduct()
        data class Error(val throwable: Throwable) : AddProduct()
    }
}

sealed class ProductDetailState {
    sealed class GetProductDetail : ProductDetailState() {
        object InFlight : GetProductDetail()
        data class Success(val productDetailEntity: ProductDetailEntity) : GetProductDetail()
        data class Error(val throwable: Throwable) : GetProductDetail()
    }

    sealed class AddProduct : ProductDetailState() {
        object InFlight : AddProduct()
        object Success : AddProduct()
        data class Error(val throwable: Throwable) : AddProduct()
    }
}
