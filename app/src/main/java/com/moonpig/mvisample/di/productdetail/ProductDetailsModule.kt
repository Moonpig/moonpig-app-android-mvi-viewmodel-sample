package com.moonpig.mvisample.di.productdetail

import com.moonpig.mvisample.domain.ProductDetailRepository
import com.moonpig.mvisample.domain.ProductDetailUseCase
import com.moonpig.mvisample.productdetail.ProductDetailTracker
import com.moonpig.mvisample.productdetail.ProductDetailViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ProductDetailsModule {

    @Provides
    fun provideProductDetailsUseCase(productDetailsRepository: ProductDetailRepository) =
            ProductDetailUseCase(productDetailsRepository)

    @Provides
    fun provideProductDetailTracker(): ProductDetailTracker =
            ProductDetailTracker()

    @Provides
    fun provideProductDetailsViewModelFactory(
            productDetailsUseCase: ProductDetailUseCase,
            productDetailTracker: ProductDetailTracker
    ): ProductDetailViewModelFactory =
            ProductDetailViewModelFactory(productDetailsUseCase, productDetailTracker)
}