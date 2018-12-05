package com.moonpig.mvisample.di.productdetail

import com.moonpig.mvisample.di.PerActivity
import com.moonpig.mvisample.productdetail.ProductDetailActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [
    ProductDetailsDataModule::class
])
interface ProductDetailsComponent {

    fun inject(productDetailsActivity: ProductDetailActivity)
}