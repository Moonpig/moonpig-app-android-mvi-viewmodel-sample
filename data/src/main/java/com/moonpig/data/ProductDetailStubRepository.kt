package com.moonpig.data

import com.moonpig.mvisample.domain.ProductDetailEntity
import com.moonpig.mvisample.domain.ProductDetailRepository
import com.moonpig.mvisample.domain.RepositoryState
import io.reactivex.Observable

class ProductDetailStubRepository : ProductDetailRepository {
    override fun getProductDetails(): Observable<RepositoryState.GetProductDetail> =
            Observable.just(RepositoryState.GetProductDetail.InFlight,
                            RepositoryState.GetProductDetail.Success(ProductDetailEntity("Advanced cat fighting gift set", "Everything your kitty needs to seriously up it's fighting game! Accessories including the pictured mace not included.", 299)))

    override fun addProductToBasket(productDetailEntity: ProductDetailEntity): Observable<RepositoryState.AddProduct> =
            Observable.just(RepositoryState.AddProduct.InFlight,
                            RepositoryState.AddProduct.Success)
}