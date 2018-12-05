package com.moonpig.mvisample.productdetail

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.moonpig.mvisample.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_product_detail.progressBar

class ProductDetailActivity : Activity(), ProductDetailView {

    private val viewModel: ProductDetailViewModel by lazy {
        ProductDetailViewModelFactory().create(ProductDetailViewModel::class.java)
    }
    private val productDetailRenderer = ProductDetailRenderer()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        bindViewModel()
    }

    private fun bindViewModel() {
        viewModel.viewState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::renderStateToView)
                .addToDisposables()
    }


    private fun Disposable.addToDisposables() =
            disposables.add(this)

    private fun renderStateToView(viewState: ProductDetailViewState) =
            productDetailRenderer.render(this, viewState)

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun isLoading(visible: Boolean) {
        progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }
}
