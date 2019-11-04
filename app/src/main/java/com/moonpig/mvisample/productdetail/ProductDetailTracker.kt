package com.moonpig.mvisample.productdetail

import android.util.Log
import com.moonpig.mvisample.mvibase.BaseTracker

class ProductDetailTracker : BaseTracker<ProductDetailScreenViewState, ProductDetailIntent, ProductDetailViewAction> {
    override fun trackIntent(intent: ProductDetailIntent) {
        Log.d(ProductDetailTracker::class.java.simpleName, "Intent: $intent")
    }

    override fun trackViewAction(viewAction: ProductDetailViewAction) {
        Log.d(ProductDetailTracker::class.java.simpleName, "ViewAction: $viewAction")
    }

    override fun trackViewState(viewState: ProductDetailScreenViewState) {
        Log.d(ProductDetailTracker::class.java.simpleName, "ViewState: $viewState")
    }
}
