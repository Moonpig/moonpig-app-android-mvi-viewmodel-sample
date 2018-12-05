package com.moonpig.mvisample.productdetail

import android.os.Bundle
import android.view.View
import com.moonpig.mvisample.MVIExampleApplication
import com.moonpig.mvisample.R
import com.moonpig.mvisample.di.productdetail.ProductDetailsComponent
import com.moonpig.mvisample.mvibase.BaseActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_product_detail.progressBar
import javax.inject.Inject

class ProductDetailActivity : BaseActivity(), ProductDetailView {

    @Inject lateinit var viewModelFactory: ProductDetailViewModelFactory

    private val viewModel: ProductDetailViewModel by lazy {
        viewModelFactory.create(ProductDetailViewModel::class.java)
    }
    private val productDetailRenderer = ProductDetailRenderer()
    private val component: ProductDetailsComponent by lazy {
        (application as MVIExampleApplication).applicationComponent.productDetailsComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        bindViewModel()
    }

    private fun bindViewModel() {
        viewModel.viewState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::renderStateToView)
                .addToDisposables()

        viewModel.bindIntents(Observable.mergeArray(
            initialIntent()
        ))
    }

    private fun renderStateToView(viewState: ProductDetailViewState) =
            productDetailRenderer.render(this, viewState)

    private fun initialIntent() = Observable.just(ProductDetailIntent.Initial)

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun isLoading(visible: Boolean) {
        progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }
}
