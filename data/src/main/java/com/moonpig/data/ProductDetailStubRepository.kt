package com.moonpig.data

import com.moonpig.mvisample.domain.entities.ProductDetail
import com.moonpig.mvisample.domain.productdetail.AddProductRequest
import com.moonpig.mvisample.domain.productdetail.ProductDetailRepository
import com.moonpig.mvisample.domain.productdetail.RepositoryState
import io.reactivex.Observable

class ProductDetailStubRepository : ProductDetailRepository {
    override fun getProductDetails(): Observable<RepositoryState.GetProductDetail> =
            Observable.just(
                    RepositoryState.GetProductDetail.InFlight,
                    RepositoryState.GetProductDetail.Success(
                            ProductDetail(
                                    "Advanced Cat Armor",
                                    "Everything your kitty needs to seriously up it's fighting game! Accessories including the pictured pole not included.",
                                    299,
                                    "https://i.postimg.cc/QtwNZdm4/cat-armor-1.jpg"
                            )
                    )
            )

    override fun addProductToBasket(addProductRequest: AddProductRequest): Observable<RepositoryState.AddProduct> =
            Observable.just(RepositoryState.AddProduct.InFlight,
                            RepositoryState.AddProduct.Success)
}