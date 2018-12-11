package com.moonpig.mvisample.di.productdetail

import com.moonpig.data.ProductDetailStubRepository
import com.moonpig.mvisample.domain.productdetail.ProductDetailRepository
import dagger.Module
import dagger.Provides

@Module
class ProductDetailDataModule {

    @Provides
    fun provideProductDetailsRepository(): ProductDetailRepository =
            ProductDetailStubRepository()
}