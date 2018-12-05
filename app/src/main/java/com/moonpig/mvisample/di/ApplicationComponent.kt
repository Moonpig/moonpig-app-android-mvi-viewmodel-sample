package com.moonpig.mvisample.di

import com.moonpig.mvisample.di.productdetail.ProductDetailsComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class
])
interface ApplicationComponent {

    fun productDetailsComponent(): ProductDetailsComponent
}