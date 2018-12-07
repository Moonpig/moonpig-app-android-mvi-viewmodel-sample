package com.moonpig.mvisample.productdetail

import com.moonpig.mvisample.mvibase.BaseViewState

data class ProductDetailScreenViewState(val getProductDetailInFlight: Boolean = false,
                                        val getProductDetailError: Throwable? = null,
                                        val productDetail: ProductDetail = ProductDetail.None,
                                        val addToBasketInFlight: Boolean = false,
                                        val addToBasketError: Throwable? = null) :
        BaseViewState