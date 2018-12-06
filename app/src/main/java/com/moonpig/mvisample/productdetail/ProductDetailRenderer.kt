package com.moonpig.mvisample.productdetail

class ProductDetailRenderer {

    fun render(view: ProductDetailView, viewState: ProductDetailViewState) {
        renderLoadingIndicator(view, viewState)
        renderProductDetail(viewState, view)
    }

    private fun renderLoadingIndicator(view: ProductDetailView, viewState: ProductDetailViewState) {
        view.isLoading(viewState.getProductDetailInFlight)
    }

    private fun renderProductDetail(viewState: ProductDetailViewState, view: ProductDetailView) {
        if (viewState.productDetail === ProductDetail.None)
            return

        view.displayName(viewState.productDetail.name)
        view.displayDescription(viewState.productDetail.description)
        view.displayPrice("£${viewState.productDetail.price}")
        view.displayImage(viewState.productDetail.imageUrl)
    }
}
