package com.moonpig.mvisample.domain.productdetail

import com.moonpig.mvisample.domain.entities.ProductDetail
import io.reactivex.Observable

interface ProductDetailRepository {
    fun getProductDetails(): Observable<RepositoryState.GetProductDetail>
    fun addProductToBasket(addProductRequest: AddProductRequest): Observable<RepositoryState.AddProduct>
}

sealed class RepositoryState {
    sealed class GetProductDetail : RepositoryState() {
        object InFlight : GetProductDetail()
        data class Success(val productDetail: ProductDetail) : GetProductDetail()
        data class Error(val throwable: Throwable) : GetProductDetail()
    }

    sealed class AddProduct : RepositoryState() {
        object InFlight : AddProduct()
        object Success : AddProduct()
        data class Error(val throwable: Throwable) : AddProduct()
    }
}

data class AddProductRequest(val productId: String, val quantity: Int)