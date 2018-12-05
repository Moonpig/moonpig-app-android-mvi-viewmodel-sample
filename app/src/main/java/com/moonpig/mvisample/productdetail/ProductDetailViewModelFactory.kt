package com.moonpig.mvisample.productdetail

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.moonpig.data.ProductDetailStubRepository
import com.moonpig.mvisample.domain.ProductDetailUseCase

class ProductDetailViewModelFactory : ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java))
            return ProductDetailViewModel(
                    ProductDetailUseCase(ProductDetailStubRepository()),
                    ProductDetailTracker()
            ) as T

        throw IllegalArgumentException("ViewModel ${modelClass.name} is not supported by this factory")
    }
}