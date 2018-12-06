package com.moonpig.mvisample.di

import com.moonpig.mvisample.di.productdetail.ProductDetailDataModule
import com.moonpig.mvisample.di.productdetail.ProductDetailsComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    ProductDetailDataModule::class
])
interface ApplicationComponent {

    fun productDetailsComponent(): ProductDetailsComponent
}