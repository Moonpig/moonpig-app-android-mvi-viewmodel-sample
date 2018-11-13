package com.moonpig.mvisample.productdetail

import android.util.Log
import com.moonpig.mvisample.mvibase.BaseTracker

class ProductDetailTracker : BaseTracker<ProductDetailViewState, ProductDetailIntent> {
    override fun trackViewState(viewState: ProductDetailViewState) {
        Log.d(ProductDetailTracker::class.java.simpleName, "ViewState: $viewState")
    }

    override fun trackIntent(intent: ProductDetailIntent) {
        Log.d(ProductDetailTracker::class.java.simpleName, "Intent: $intent")
    }
}
