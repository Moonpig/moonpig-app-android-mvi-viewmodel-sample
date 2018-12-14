package com.moonpig.mvisample.productdetail

import android.content.Context
import android.content.Intent
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.moonpig.mvisample.MVIExampleApplication
import com.moonpig.mvisample.R
import com.moonpig.mvisample.databinding.ActivityProductDetailBinding
import com.moonpig.mvisample.di.productdetail.ProductDetailsComponent
import com.moonpig.mvisample.mvibase.BaseActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class ProductDetailActivity : BaseActivity() {
    @Inject lateinit var viewModelFactory: ProductDetailViewModelFactory

    private val viewModel: ProductDetailViewModel by lazy {
        ViewModelProviders.of(this, this.viewModelFactory).get(ProductDetailViewModel::class.java)
    }

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
    }

    private fun initialIntent() = Observable.just(ProductDetailIntent.Initial(
            intent.getStringExtra(productIdKey) ?: ""
    ))

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
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
