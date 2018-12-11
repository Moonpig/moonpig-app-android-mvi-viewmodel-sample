package com.moonpig.mvisample.domain.productdetail

import com.moonpig.mvisample.domain.entities.ProductDetail
import com.moonpig.mvisample.domain.mvibase.BaseResult

sealed class ProductDetailResult : BaseResult {
    sealed class GetProductDetail : ProductDetailResult() {
        object InFlight : GetProductDetail()
        data class Success(val productDetail: ProductDetail) : GetProductDetail()

        data class Error(val throwable: Throwable) : GetProductDetail()
    }

    sealed class AddProduct : ProductDetailResult() {
        object InFlight : AddProduct()
        object Success : AddProduct()
        data class Error(val throwable: Throwable) : AddProduct()
    }
}
