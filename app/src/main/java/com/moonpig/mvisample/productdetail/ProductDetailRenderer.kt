package com.moonpig.mvisample.productdetail

class ProductDetailRenderer {

    fun render(view: ProductDetailView, viewState: ProductDetailScreenViewState) {
        renderLoadingIndicator(view, viewState)
        renderProductDetail(view, viewState)
    }

    private fun renderLoadingIndicator(view: ProductDetailView, viewState: ProductDetailScreenViewState) {
        view.isLoading(viewState.getProductDetailInFlight)
    }

    private fun renderProductDetail(view: ProductDetailView, viewState: ProductDetailScreenViewState) {
        viewState.productDetail?.let {
            view.displayName(it.name)
            view.displayDescription(it.description)
            view.displayPrice("£${it.price}")
            view.displayImage(it.imageUrl)
        }
    }
}
