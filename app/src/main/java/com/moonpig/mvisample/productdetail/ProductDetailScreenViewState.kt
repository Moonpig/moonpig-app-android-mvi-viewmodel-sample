package com.moonpig.mvisample.productdetail

import com.moonpig.mvisample.mvibase.BaseViewState
import com.moonpig.mvisample.mvibase.Visibility

data class ProductDetailScreenViewState(val loadingIndicatorVisibility: Visibility = Visibility.GONE,
                                        val productDetailErrorVisibility: Visibility = Visibility.GONE,
                                        val productDetail: ProductDetailViewState? = null,
                                        val addToBasketInFlight: Boolean = false,
                                        val addToBasketError: Throwable? = null) :
        BaseViewState

data class ProductDetailViewState(
        val name: String = "",
        val description: String = "",
        val price: String = "",
        val imageUrl: String = ""
)