package com.moonpig.mvisample.productdetail

interface ProductDetailView {
    fun isLoading(visible: Boolean)
    fun displayPrice(price: String)
    fun displayImage(imageUrl: String)
    fun showLoadingError(isVisible: Boolean)
}
