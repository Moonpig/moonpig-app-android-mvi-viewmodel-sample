package com.moonpig.mvisample.domain.productdetail

import com.moonpig.mvisample.domain.mvibase.BaseAction

sealed class ProductDetailAction : BaseAction {
    data class LoadProductDetail(val productId:String) : ProductDetailAction()
    data class AddProductToBasket(val productId: String, val quantity: Int) : ProductDetailAction()
}

