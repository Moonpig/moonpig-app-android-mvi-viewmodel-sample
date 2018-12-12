package com.moonpig.mvisample.productdetail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.moonpig.mvisample.MVIExampleApplication
import com.moonpig.mvisample.R
import com.moonpig.mvisample.databinding.ActivityProductDetailBinding
import com.moonpig.mvisample.di.productdetail.ProductDetailsComponent
import com.moonpig.mvisample.mvibase.BaseActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_product_detail.errorMessage
import kotlinx.android.synthetic.main.activity_product_detail.price
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
    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail)
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

    private fun renderStateToView(viewState: ProductDetailScreenViewState) {
        binding.viewState = viewState
        productDetailRenderer.render(this, viewState)
    }

    private fun initialIntent() = Observable.just(ProductDetailIntent.Initial(
            intent.getStringExtra(productIdKey) ?: ""
    ))

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun isLoading(visible: Boolean) {
        progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun displayPrice(price: String) {
        this.price.text = price
    }

    override fun showLoadingError(isVisible: Boolean) {
        errorMessage.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    companion object {
        private const val productIdKey = "productIdKey"

        @JvmStatic
        fun intentForProduct(context: Context, productId: String): Intent {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra(productIdKey, productId)
            return intent
        }
    }
}
