package com.moonpig.mvisample.screentests.di

import com.moonpig.mvisample.domain.productdetail.ProductDetailRepository
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import dagger.Module
import dagger.Provides
import io.reactivex.Observable

@Module
class MockProductDetailDataModule {

    companion object {
        val productDetailRepository: ProductDetailRepository = mock()

        init {
            given(productDetailRepository.getProductDetails()).willReturn(Observable.never())
        }
    }

    @Provides
    fun provideMockProductDetailRepository(): ProductDetailRepository = productDetailRepository
}
