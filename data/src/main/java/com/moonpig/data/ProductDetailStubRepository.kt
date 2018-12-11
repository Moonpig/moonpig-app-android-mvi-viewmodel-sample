package com.moonpig.data

import com.moonpig.mvisample.domain.entities.ProductDetail
import com.moonpig.mvisample.domain.productdetail.AddProductRequest
import com.moonpig.mvisample.domain.productdetail.ProductDetailRepository
import com.moonpig.mvisample.domain.productdetail.RepositoryState
import io.reactivex.Observable

class ProductDetailStubRepository : ProductDetailRepository {
    override fun getProductDetails(productId: String): Observable<RepositoryState.GetProductDetail> =
            when (productId) {
                "1" -> respondWithCatArmor()
                else -> respondWithLoadError()
            }

    private fun respondWithCatArmor() =
            Observable.just(
                    RepositoryState.GetProductDetail.InFlight,
                    RepositoryState.GetProductDetail.Success(
                            ProductDetail(
                                    "Advanced Cat Armor",
                                    "Everything your kitty needs to seriously up it's fighting game! This stylish armor will protect your cat from ordinary piercing and blunt damage. Accessories including the pictured pole not included.",
                                    299,
                                    "https://i.postimg.cc/QtwNZdm4/cat-armor-1.jpg"
                            )
                    )
            )

    private fun respondWithLoadError() =
            Observable.just(
                    RepositoryState.GetProductDetail.InFlight,
                    RepositoryState.GetProductDetail.Error(RuntimeException())
            )

    override fun addProductToBasket(addProductRequest: AddProductRequest): Observable<RepositoryState.AddProduct> =
            Observable.just(RepositoryState.AddProduct.InFlight,
                            RepositoryState.AddProduct.Success)
}