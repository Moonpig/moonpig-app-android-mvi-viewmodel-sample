package com.moonpig.mvisample.productdetail

interface ProductDetailView {
    fun isLoading(visible: Boolean)
    fun showLoadingError(isVisible: Boolean)
}
