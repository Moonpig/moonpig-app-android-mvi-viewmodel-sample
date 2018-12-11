package com.moonpig.mvisample.domain.productdetail

import com.moonpig.mvisample.domain.mvibase.BaseAction

sealed class ProductDetailAction : BaseAction {
    object LoadProductDetail : ProductDetailAction()
    data class AddProductToBasket(val productId: String, val quantity: Int) : ProductDetailAction()
}

