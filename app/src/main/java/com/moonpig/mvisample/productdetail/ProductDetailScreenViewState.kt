package com.moonpig.mvisample.productdetail

import com.moonpig.mvisample.mvibase.BaseViewState

data class ProductDetailScreenViewState(val getProductDetailInFlight: Boolean = false,
                                        val getProductDetailError: Throwable? = null,
                                        val getProductDetailSuccess: ProductDetailViewState? = null,
                                        val addToBasketInFlight: Boolean = false,
                                        val addToBasketError: Throwable? = null) :
        BaseViewState

data class ProductDetailViewState(
        val name: String = "",
        val description: String = "",
        val price: Int = 0,
        val imageUrl: String = ""
)