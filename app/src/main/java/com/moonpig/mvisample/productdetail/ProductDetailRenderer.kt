package com.moonpig.mvisample.productdetail

class ProductDetailRenderer {

    fun render(view: ProductDetailView, viewState: ProductDetailScreenViewState) {
        renderErrorIndicator(view, viewState)
    }

    private fun renderErrorIndicator(view: ProductDetailView, viewState: ProductDetailScreenViewState) {
        view.showLoadingError(viewState.getProductDetailError != null)
    }
}
