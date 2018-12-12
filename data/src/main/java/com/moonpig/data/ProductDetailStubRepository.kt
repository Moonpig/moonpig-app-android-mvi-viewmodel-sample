package com.moonpig.data

import com.moonpig.mvisample.domain.entities.ProductDetail
import com.moonpig.mvisample.domain.productdetail.AddProductRequest
import com.moonpig.mvisample.domain.productdetail.ProductDetailRepository
import com.moonpig.mvisample.domain.productdetail.RepositoryState
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class ProductDetailStubRepository : ProductDetailRepository {
    override fun getProductDetails(productId: String): Observable<RepositoryState.GetProductDetail> =
            when (productId) {
                catArmourProductId -> respondWithCatArmour()
                else -> respondWithLoadError()
            }

    private fun respondWithCatArmour() =
            Observable.merge(
                    Observable.just(RepositoryState.GetProductDetail.InFlight),
                    Observable.just(
                            RepositoryState.GetProductDetail.Success(
                                    ProductDetail(
                                            "Advanced Cat Armour",
                                            "Everything your kitty needs to seriously up it's fighting game! This stylish armour will protect your cat from ordinary piercing and blunt damage. Accessories including the pictured pole not included.",
                                            299,
                                            "https://i.postimg.cc/QtwNZdm4/cat-armor-1.jpg"
                                    )
                            )
                    ).delay(1, TimeUnit.SECONDS)
            )

    private fun respondWithLoadError() =
            Observable.merge(
                    Observable.just(RepositoryState.GetProductDetail.InFlight),
                    Observable.just(RepositoryState.GetProductDetail.Error(RuntimeException())).delay(2, TimeUnit.SECONDS)
            )

    override fun addProductToBasket(addProductRequest: AddProductRequest): Observable<RepositoryState.AddProduct> =
            Observable.just(RepositoryState.AddProduct.InFlight,
                            RepositoryState.AddProduct.Success)

    companion object {
        private const val catArmourProductId = "1"
    }
}