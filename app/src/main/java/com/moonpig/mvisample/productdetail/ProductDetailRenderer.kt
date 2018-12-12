package com.moonpig.mvisample.productdetail

class ProductDetailRenderer {

    fun render(view: ProductDetailView, viewState: ProductDetailScreenViewState) {
        renderLoadingIndicator(view, viewState)
        renderErrorIndicator(view, viewState)
    }

    private fun renderLoadingIndicator(view: ProductDetailView, viewState: ProductDetailScreenViewState) {
        view.isLoading(viewState.getProductDetailInFlight)
    }

    private fun renderErrorIndicator(view: ProductDetailView, viewState: ProductDetailScreenViewState) {
        view.showLoadingError(viewState.getProductDetailError != null)
    }
}
