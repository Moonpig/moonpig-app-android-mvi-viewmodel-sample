package com.moonpig.mvisample.productdetail

class ProductDetailRenderer {

    fun render(view: ProductDetailView, viewState: ProductDetailViewState) {
        view.isLoading(viewState.getProductDetailInFlight)
    }
}