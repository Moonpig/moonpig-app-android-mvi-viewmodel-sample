package com.moonpig.mvisample.productdetail

interface ProductDetailView {
    fun isLoading(visible: Boolean)
    fun displayPrice(price: String)
    fun showLoadingError(isVisible: Boolean)
}
